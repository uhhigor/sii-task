package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.exception.ProductBuilderException;
import org.uhhigor.siitask.exception.ProductPriceBuilderException;
import org.uhhigor.siitask.exception.ProductServiceException;
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
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product.ProductDto productDto) throws ProductServiceException {
        List<ProductPrice> productPrices = savePricesFromDto(productDto);
        try {
            Product product = new ProductBuilder()
                    .name(productDto.getName())
                    .description(productDto.getDescription())
                    .prices(productPrices)
                    .build();
            return productRepository.save(product);
        } catch (ProductBuilderException e) {
            productPriceRepository.deleteAll(productPrices);
            throw new ProductServiceException("Invalid product", e);
        }
    }

    public Product getById(Long id) throws ProductServiceException {
        return productRepository.findById(id).orElseThrow(() -> new ProductServiceException("Product not found"));
    }

    public Product updateProduct(Product.ProductDto productDto, Long id) throws ProductServiceException {
        Product product = getById(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());

        List<ProductPrice> productPrices = product.getPrices();
        product.setPrices(null);
        productPriceRepository.deleteAll(productPrices);

        productPrices = savePricesFromDto(productDto);
        product.setPrices(productPrices);
        return productRepository.save(product);
    }

    private List<ProductPrice> savePricesFromDto(Product.ProductDto productDto) throws ProductServiceException {
        List<ProductPrice> prices = new ArrayList<>();
        for(ProductPrice.ProductPriceDto priceDto : productDto.getPrices()) {
            try {
                prices.add(new ProductPriceBuilder()
                        .currency(priceDto.getCurrency())
                        .price(priceDto.getPrice())
                        .build());
            } catch (ProductPriceBuilderException e) {
                throw new ProductServiceException("Invalid product price", e);
            }
        }
        Iterable<ProductPrice> newProductPrices = productPriceRepository.saveAll(prices);
        List<ProductPrice> result = new ArrayList<>();
        newProductPrices.forEach(result::add);
        return result;
    }

    public void deleteProduct(Long id) throws ProductServiceException {
        Product product = getById(id);
        productRepository.delete(product);
    }
}
