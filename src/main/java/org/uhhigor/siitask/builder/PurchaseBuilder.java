package org.uhhigor.siitask.builder;

import org.uhhigor.siitask.exception.PurchaseBuilderException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.model.Purchase;

import java.util.Currency;
import java.util.Date;

public class PurchaseBuilder {
    private Date date;
    private Product product;
    private PromoCode promoCode;
    private Currency currency;

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

    public PurchaseBuilder promoCode(PromoCode promoCode) throws PurchaseBuilderException {
        if(promoCode == null) {
            throw new PurchaseBuilderException("Promo code cannot be null");
        }
        this.promoCode = promoCode;
        return this;
    }

    public PurchaseBuilder currency(String currencyCode) throws PurchaseBuilderException {
        try {
            this.currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new PurchaseBuilderException("Currency code is invalid");
        }
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

        boolean priceFound = false;
        for(ProductPrice productPrice : product.getPrices()) {
            if(productPrice.getCurrency().equals(currency)) {
                priceFound = true;
                purchase.setRegularPrice(productPrice.getPrice());
                break;
            }
        }

        if(!priceFound) {
            throw new PurchaseBuilderException("Product does not have a price in the specified currency");
        }

        if(promoCode != null) {
            if(!promoCode.getEligibleProducts().contains(product)) {
                throw new PurchaseBuilderException("Product is not eligible for the promo code");
            }
            if(promoCode.getExpirationDate().before(date)) {
                throw new PurchaseBuilderException("Promo code is expired");
            }
            if(promoCode.getUsesLeft() <= 0) {
                throw new PurchaseBuilderException("Promo code has no uses left");
            }
            if(!promoCode.getCurrency().equals(currency)) {
                throw new PurchaseBuilderException("Promo code currency does not match purchase currency");
            }

            purchase.setDiscountApplied(promoCode.getDiscountAmount());
            purchase.setFinalPrice(purchase.getRegularPrice() - promoCode.getDiscountAmount());
        } else {
            purchase.setDiscountApplied(0.0);
            purchase.setFinalPrice(purchase.getRegularPrice());
        }

        return purchase;
    }


}
