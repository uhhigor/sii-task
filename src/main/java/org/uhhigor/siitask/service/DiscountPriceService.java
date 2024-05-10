package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeException;
import org.uhhigor.siitask.exception.promocode.PromoCodeExpiredException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;

import java.util.Date;

@Service
public class DiscountPriceService {
    public double getDiscountPrice(Product product, PromoCode promoCode) throws PromoCodeException {
        double discount = getDiscount(product, promoCode);
        ProductPrice productPrice = product.getProductPriceByCurrency(promoCode.getCurrency());
        double discountPrice = productPrice.getPrice() - discount;
        if(discountPrice < 0) {
            return 0;
        }
        return discountPrice;
    }

    public double getDiscount(Product product, PromoCode promoCode) throws PromoCodeException {
        ProductPrice productPrice = product.getProductPriceByCurrency(promoCode.getCurrency());
        if (productPrice == null) {
            throw new CurrenciesDoNotMatchException("Error while getting discount price: Product currency does not match promo code currency");
        }
        if(promoCode.getExpirationDate().before(new Date())) {
            throw new PromoCodeExpiredException("Error while getting discount price: Promo code is expired");
        }
        if(promoCode.getUsesLeft() <= 0) {
            throw new PromoCodeExpiredException("Error while getting discount price: Promo code has no uses left");
        }
        double price = productPrice.getPrice();

        if(promoCode.getType().equals(PromoCode.DiscountType.PERCENTAGE)) {
            return price * promoCode.getDiscountAmount() / 100;
        } else if(promoCode.getType().equals(PromoCode.DiscountType.FIXED)) {
            return promoCode.getDiscountAmount();
        }
        throw new PromoCodeException("Error while getting discount price: Promo code type not recognized");
    }
}
