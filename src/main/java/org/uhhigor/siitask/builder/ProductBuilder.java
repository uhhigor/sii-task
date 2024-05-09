package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.product.*;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;

import java.util.List;

public class ProductBuilder {
    private String name;
    private String description;
    private List<ProductPrice> prices;

    public ProductBuilder name(String name) throws ProductNameIncorrectException {
        if(name.isEmpty()) {
            throw new ProductNameIncorrectException("Name cannot be empty");
        }
        this.name = name;
        return this;
    }

    public ProductBuilder description(String description) throws ProductDescriptionIncorrectException {
        if(description == null || description.isEmpty()) {
            throw new ProductDescriptionIncorrectException("Description cannot be empty");
        }
        this.description = description;
        return this;
    }

    public ProductBuilder prices(List<ProductPrice> prices) throws ProductPricesEmptyException {
        if(prices == null || prices.isEmpty()) {
            throw new ProductPricesEmptyException("Prices cannot be null or empty");
        }
        this.prices = prices;
        return this;
    }

    public Product build() throws ProductException {
        if(name == null || name.isEmpty()) {
            throw new ProductException("Name is required");
        }
        if(prices == null || prices.isEmpty()) {
            throw new ProductException("Prices are required");
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrices(prices);

        return product;
    }
}
