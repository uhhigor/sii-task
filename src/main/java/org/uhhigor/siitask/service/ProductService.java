package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.exception.product.ProductException;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.product.ProductPriceException;
import org.uhhigor.siitask.exception.product.ProductServiceException;
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

    public ProductPrice addProductPrice(double price, String currency) throws ProductServiceException {
        try {
            ProductPrice productPrice = new ProductPriceBuilder()
                    .price(price)
                    .currency(currency)
                    .build();
            return productPriceRepository.save(productPrice);
        } catch (ProductPriceException e) {
            throw new ProductServiceException("Error while adding new product price: " + e.getMessage(), e);
        }
    }
    public Product addProduct(String name, String description, List<ProductPrice> prices) throws ProductServiceException {
        try {
            Product product = new ProductBuilder()
                            .name(name)
                            .description(description)
                            .prices(prices)
                            .build();
            return productRepository.save(product);
        } catch (ProductException e) {
            productPriceRepository.deleteAll(prices);
            throw new ProductServiceException("Error while adding new product: " + e.getMessage(), e);
        }
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product updateProduct(Long id, String name, String description, List<ProductPrice> prices) throws ProductServiceException {
        System.out.println("Updating product with id: " + id);
        System.out.println(getProducts());
        Product product = getProductById(id);
        product.setName(name);
        product.setDescription(description);

        List<ProductPrice> productPrices = product.getPrices();
        productPriceRepository.deleteAll(productPrices);
        product.setPrices(prices);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
