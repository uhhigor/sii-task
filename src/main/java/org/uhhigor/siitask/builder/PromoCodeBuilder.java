package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.promocode.*;
import org.uhhigor.siitask.model.PromoCode;

import java.util.Currency;
import java.util.Date;

public class PromoCodeBuilder {
    private String code;
    private Date expirationDate;
    private Double discountAmount;
    private Currency currency;
    private Integer usesLeft;

    private PromoCode.DiscountType type;

    public PromoCodeBuilder code(String code) throws PromoCodeIncorrectException {
        if(code == null) {
            throw new PromoCodeIncorrectException("Code cannot be null");
        }
        if(code.length() < 3 || code.length() > 24){
            throw new PromoCodeIncorrectException("Code length must be between 3 and 24 characters");
        }
        if(!code.matches("^[a-zA-Z0-9]*$")) {
            throw new PromoCodeIncorrectException("Code must contain only alphanumeric characters");
        }
        this.code = code;
        return this;
    }

    public PromoCodeBuilder expirationDate(Date expirationDate) throws PromoCodeExpirationDateInvalidException {
        if(expirationDate == null || expirationDate.before(new Date())) {
            throw new PromoCodeExpirationDateInvalidException("Expiration date cannot be null or in the past");
        }
        this.expirationDate = expirationDate;
        return this;
    }

    public PromoCodeBuilder discountAmount(Double discountAmount) throws PromoCodeDiscountInvalidException {
        if(discountAmount == null || discountAmount <= 0) {
            throw new PromoCodeDiscountInvalidException("Discount amount must be greater than 0");
        }
        this.discountAmount = discountAmount;
        return this;
    }

    public PromoCodeBuilder currency(String currencyCode) throws PromoCodeCurrencyInvalidException {
        try {
            this.currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new PromoCodeCurrencyInvalidException("Currency code is invalid: " + currencyCode);
        }
        return this;
    }

    public PromoCodeBuilder uses(Integer uses) throws PromoCodeUsesInvalidException {
        if(uses == null || uses <= 0) {
            throw new PromoCodeUsesInvalidException("Uses must be greater than 0");
        }
        this.usesLeft = uses;
        return this;
    }

    public PromoCodeBuilder type(String type) throws PromoCodeIncorrectException {
        if(type == null || type.isEmpty()) {
            throw new PromoCodeIncorrectException("Type cannot be null or empty");
        }
        try {
            this.type = PromoCode.DiscountType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new PromoCodeIncorrectException("Type is invalid: " + type);
        }
        return this;
    }

    public PromoCode build() throws PromoCodeException {
        if(expirationDate == null) {
            throw new PromoCodeException("Expiration date is required");
        }
        if(discountAmount == null) {
            throw new PromoCodeException("Discount amount is required");
        }
        if(currency == null) {
            throw new PromoCodeException("Currency is required");
        }
        if(usesLeft == null) {
            throw new PromoCodeException("Uses is required");
        }
        if(type == null) {
            throw new PromoCodeException("Discount type is required");
        }

        if(type == PromoCode.DiscountType.PERCENTAGE && discountAmount > 100) {
            throw new PromoCodeDiscountInvalidException("Discount amount must be between 0 and 100 for percentage based promo codes");
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(code);
        promoCode.setExpirationDate(expirationDate);
        promoCode.setDiscountAmount(discountAmount);
        promoCode.setCurrency(currency);
        promoCode.setUsesLeft(usesLeft);
        promoCode.setType(type);
        promoCode.setTimesUsed(0);
        return promoCode;
    }
}
