package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.PromoCodeBuilderException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.PromoCode;

import java.util.Currency;
import java.util.Date;
import java.util.List;

public class PromoCodeBuilder {
    private String code;
    private Date expirationDate;
    private Double discountAmount;
    private Currency currency;
    private Integer usesLeft;
    private List<Product> eligibleProducts;

    public PromoCodeBuilder code(String code) throws PromoCodeBuilderException {
        if(code == null) {
            throw new PromoCodeBuilderException("Code cannot be null");
        }
        if(code.length() < 3 || code.length() > 24){
            throw new PromoCodeBuilderException("Code length must be between 3 and 24 characters");
        }
        if(!code.matches("^[a-zA-Z0-9]*$")) {
            throw new PromoCodeBuilderException("Code must contain only alphanumeric characters");
        }
        this.code = code;
        return this;
    }

    public PromoCodeBuilder expirationDate(Date expirationDate) throws PromoCodeBuilderException {
        if(expirationDate == null || expirationDate.before(new Date())) {
            throw new PromoCodeBuilderException("Expiration date cannot be null or in the past");
        }
        this.expirationDate = expirationDate;
        return this;
    }

    public PromoCodeBuilder discountAmount(Double discountAmount) throws PromoCodeBuilderException {
        if(discountAmount == null || discountAmount <= 0) {
            throw new PromoCodeBuilderException("Discount amount must be greater than 0");
        }
        this.discountAmount = discountAmount;
        return this;
    }

    public PromoCodeBuilder currency(String currencyCode) throws PromoCodeBuilderException {
        try {
            this.currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new PromoCodeBuilderException("Currency code is invalid");
        }
        return this;
    }

    public PromoCodeBuilder uses(Integer uses) throws PromoCodeBuilderException {
        if(uses == null || uses <= 0) {
            throw new PromoCodeBuilderException("Uses must be greater than 0");
        }
        this.usesLeft = uses;
        return this;
    }

    public PromoCodeBuilder eligibleProducts(List<Product> products) throws PromoCodeBuilderException {
        if(products == null || products.isEmpty()) {
            throw new PromoCodeBuilderException("Eligible products list cannot be null or empty");
        }
        this.eligibleProducts = products;
        return this;
    }

    public PromoCode build() throws PromoCodeBuilderException {
        if(expirationDate == null) {
            throw new PromoCodeBuilderException("Expiration date is required");
        }
        if(discountAmount == null) {
            throw new PromoCodeBuilderException("Discount amount is required");
        }
        if(currency == null) {
            throw new PromoCodeBuilderException("Currency is required");
        }
        if(usesLeft == null) {
            throw new PromoCodeBuilderException("Uses is required");
        }
        if(eligibleProducts == null) {
            throw new PromoCodeBuilderException("Eligible products list is required");
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(code);
        promoCode.setExpirationDate(expirationDate);
        promoCode.setDiscountAmount(discountAmount);
        promoCode.setCurrency(currency);
        promoCode.setUsesLeft(usesLeft);
        promoCode.setEligibleProducts(eligibleProducts);
        return promoCode;
    }
}
