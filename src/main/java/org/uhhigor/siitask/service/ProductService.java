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

    public ProductPrice addProductPrice(ProductPrice productPrice) {
            return productPriceRepository.save(productPrice);
    }

    public List<ProductPrice> addProductPrices(List<ProductPrice> productPrices) {
        return (List<ProductPrice>) productPriceRepository.saveAll(productPrices);
    }

    public void deleteProductPrices(List<ProductPrice> prices) {
        productPriceRepository.deleteAll(prices);
    }
    public Product addProduct(Product product) {
        product.setPrices(addProductPrices(product.getPrices()));
        return productRepository.save(product);
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product updateProduct(Long id, Product product) throws ProductServiceException {
        Product originalProduct = getProductById(id);
        originalProduct.setName(product.getName());
        originalProduct.setDescription(product.getDescription());
        System.out.println("Deleting prices");
        deleteProductPrices(originalProduct.getPrices());
        System.out.println("Adding prices");
        originalProduct.setPrices(addProductPrices(product.getPrices()));

        return productRepository.save(originalProduct);
    }

    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
