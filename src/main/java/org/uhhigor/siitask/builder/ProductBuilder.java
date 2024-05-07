package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.ProductBuilderException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;

import java.util.Currency;
import java.util.List;

public class ProductBuilder {
    private String name;
    private String description;
    private List<ProductPrice> prices;

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder prices(List<ProductPrice> prices) {
        this.prices = prices;
        return this;
    }

    public Product build() throws ProductBuilderException {
        if(name == null || name.isEmpty()) {
            throw new ProductBuilderException("Name is required");
        }
        if(prices == null || prices.isEmpty()) {
            throw new ProductBuilderException("Prices are required");
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrices(prices);

        return product;
    }
}
