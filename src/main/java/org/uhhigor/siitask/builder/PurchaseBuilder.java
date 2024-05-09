package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.purchase.PurchaseBuilderException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.Purchase;

import java.util.Currency;
import java.util.Date;

public class PurchaseBuilder {
    private Date date;
    private Product product;
    private Currency currency;
    private Double discount;
    private Double regularPrice;

    public PurchaseBuilder date(Date date) throws PurchaseBuilderException {
        if(date == null) {
            throw new PurchaseBuilderException("Date cannot be null");
        }
        if(date.after(new Date())) {
            throw new PurchaseBuilderException("Date cannot be in the future");
        }
        this.date = date;
        return this;
    }

    public PurchaseBuilder product(Product product) throws PurchaseBuilderException {
        if(product == null) {
            throw new PurchaseBuilderException("Product cannot be null");
        }
        this.product = product;
        return this;
    }


    public PurchaseBuilder currency(Currency currency) throws PurchaseBuilderException {
        if(currency == null) {
            throw new PurchaseBuilderException("Currency cannot be null");
        }
        this.currency = currency;
        return this;
    }

    public PurchaseBuilder discount(Double discount) throws PurchaseBuilderException {
        if(discount == null || discount < 0) {
            throw new PurchaseBuilderException("Discount must be greater than or equal to 0");
        }
        this.discount = discount;
        return this;
    }

    public PurchaseBuilder regularPrice(Double regularPrice) throws PurchaseBuilderException {
        if(regularPrice == null || regularPrice <= 0) {
            throw new PurchaseBuilderException("Regular price must be greater than 0");
        }
        this.regularPrice = regularPrice;
        return this;
    }

    public Purchase build() throws PurchaseBuilderException {
        Purchase purchase = new Purchase();

        if(date == null) {
            throw new PurchaseBuilderException("Date cannot be null");
        }
        purchase.setDate(date);

        if(product == null) {
            throw new PurchaseBuilderException("Product cannot be null");
        }
        purchase.setProduct(product);

        if(currency == null) {
            throw new PurchaseBuilderException("Currency cannot be null");
        }
        purchase.setCurrency(currency);

        if(regularPrice == null) {
            throw new PurchaseBuilderException("Regular price cannot be null");
        }
        purchase.setRegularPrice(regularPrice);

        if(discount == null) {
            discount = 0.0;
        }
        purchase.setDiscountApplied(discount);

        return purchase;
    }


}
