package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.exception.product.*;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.repository.ProductPriceRepository;
import org.uhhigor.siitask.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    public ProductService(ProductRepository productRepository, ProductPriceRepository productPriceRepository) {
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
    }
    public List<Product> getProducts() {
        List<Product> result = new ArrayList<>();
        productRepository.findAll().forEach(result::add);
        return result;
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        List<Product> result = new ArrayList<>();
        productRepository.findAllById(ids).forEach(result::add);
        return result;
    }

    public Product addProduct(Product.ProductDto productDto) throws ProductServiceException {
        List<ProductPrice> productPrices = savePricesFromDto(productDto.getPrices());
        try {
            Product product;
            if(!productDto.getDescription().isEmpty()) {
                product = new ProductBuilder()
                        .name(productDto.getName())
                        .description(productDto.getDescription())
                        .prices(productPrices)
                        .build();
            } else {
                product = new ProductBuilder()
                        .name(productDto.getName())
                        .prices(productPrices)
                        .build();
            }
            return productRepository.save(product);
        } catch (ProductBuilderException e) {
            productPriceRepository.deleteAll(productPrices);
            throw new ProductServiceException("Invalid product", e);
        }
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product updateProduct(Product.ProductDto productDto, Long id) throws ProductServiceException {
        Product product = getProductById(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());

        List<ProductPrice> productPrices = product.getPrices();
        product.setPrices(null);
        productPriceRepository.deleteAll(productPrices);

        try {
            productPrices = savePricesFromDto(productDto.getPrices());
        } catch (ProductServiceException e) {
            throw new ProductServiceException("Error while updating product: " + e.getMessage(), e);
        }
        product.setPrices(productPrices);
        return productRepository.save(product);
    }

    private List<ProductPrice> savePricesFromDto(List<ProductPrice.ProductPriceDto> productPriceDtos) throws ProductServiceException {
        List<ProductPrice> prices = new ArrayList<>();
        for(ProductPrice.ProductPriceDto priceDto : productPriceDtos) {
            try {
                prices.add(new ProductPriceBuilder()
                        .currency(priceDto.getCurrency())
                        .price(priceDto.getPrice())
                        .build());
            } catch (ProductPriceException e) {
                throw new ProductServiceException("Error while saving product prices: " + e.getMessage(), e);
            }
        }
        Iterable<ProductPrice> newProductPrices = productPriceRepository.saveAll(prices);
        List<ProductPrice> result = new ArrayList<>();
        newProductPrices.forEach(result::add);
        return result;
    }

    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
