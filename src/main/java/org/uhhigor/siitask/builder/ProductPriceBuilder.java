package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.ProductPriceBuilderException;

import java.util.Currency;
import org.uhhigor.siitask.model.ProductPrice;

public class ProductPriceBuilder {
    private Double price;
    private Currency currency;

    public ProductPriceBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public ProductPriceBuilder currency(String currencyCode) throws ProductPriceBuilderException {
        try {
            this.currency = Currency.getInstance(currencyCode);
            return this;
        } catch (IllegalArgumentException e) {
            throw new ProductPriceBuilderException("Invalid currency code: " + currencyCode, e);
        }
    }

    public ProductPrice build() throws ProductPriceBuilderException {
        if(price == null) {
            throw new ProductPriceBuilderException("Price is required");
        }
        if(currency == null) {
            throw new ProductPriceBuilderException("Currency is required");
        }
        ProductPrice productPrice = new ProductPrice();
        productPrice.setPrice(price);
        productPrice.setCurrency(currency);

        return productPrice;
    }
}
