package org.uhhigor.siitask.builder;

import java.util.Currency;

import org.uhhigor.siitask.exception.product.ProductPriceException;
import org.uhhigor.siitask.exception.product.ProductPriceNotValidException;
import org.uhhigor.siitask.model.ProductPrice;

public class ProductPriceBuilder {
    private Double price;
    private Currency currency;

    public ProductPriceBuilder price(Double price) throws ProductPriceNotValidException {
        if(price == null || price <= 0) {
            throw new ProductPriceNotValidException("Price must be greater than 0");
        }
        this.price = price;
        return this;
    }

    public ProductPriceBuilder currency(String currencyCode) throws ProductPriceNotValidException {
        try {
            this.currency = Currency.getInstance(currencyCode);
            return this;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ProductPriceNotValidException("Invalid currency code: " + currencyCode, e);
        }
    }

    public ProductPrice build() throws ProductPriceException {
        if(price == null) {
            throw new ProductPriceException("Price is required");
        }
        if(currency == null) {
            throw new ProductPriceException("Currency is required");
        }
        ProductPrice productPrice = new ProductPrice();
        productPrice.setPrice(price);
        productPrice.setCurrency(currency);

        return productPrice;
    }
}
