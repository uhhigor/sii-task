package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.purchase.*;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.Purchase;

import java.util.Currency;
import java.util.Date;

public class PurchaseBuilder {
    private Date date;
    private Product product;
    private Currency currency;
    private Double discount;

    public PurchaseBuilder date(Date date) throws PurchaseDateInvalidException {
        if(date == null) {
            throw new PurchaseDateInvalidException("Date cannot be null");
        }
        if(date.after(new Date())) {
            throw new PurchaseDateInvalidException("Date cannot be in the future");
        }
        this.date = date;
        return this;
    }

    public PurchaseBuilder product(Product product) throws PurchaseProductNullException {
        if(product == null) {
            throw new PurchaseProductNullException("Product cannot be null");
        }
        this.product = product;
        return this;
    }


    public PurchaseBuilder currency(Currency currency) throws PurchaseCurrencyNullException {
        if(currency == null) {
            throw new PurchaseCurrencyNullException("Currency cannot be null");
        }
        this.currency = currency;
        return this;
    }

    public PurchaseBuilder discount(Double discount) throws PurchaseDiscountInvalidException {
        if(discount == null || discount < 0) {
            throw new PurchaseDiscountInvalidException("Discount must be greater than or equal to 0");
        }
        this.discount = discount;
        return this;
    }

    public Purchase build() throws PurchaseException {
        Purchase purchase = new Purchase();

        if(date == null) {
            throw new PurchaseException("Date cannot be null");
        }
        purchase.setDate(date);

        if(product == null) {
            throw new PurchaseException("Product cannot be null");
        }
        purchase.setProduct(product);

        if(currency == null) {
            throw new PurchaseException("Currency cannot be null");
        }
        purchase.setCurrency(currency);

        purchase.setRegularPrice(product.getProductPriceByCurrency(currency).getPrice());

        if(discount == null) {
            discount = 0.0;
        }
        purchase.setDiscountApplied(discount);

        return purchase;
    }


}
