package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeExpiredException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.PromoCode;

import java.util.Date;

@Service
public class DiscountPriceService {
    public double getDiscountPrice(Product product, PromoCode promoCode) throws PromoCodeExpiredException, CurrenciesDoNotMatchException {
        Double productPrice = product.getProductPriceByCurrency(promoCode.getCurrency()).getPrice();
        if (productPrice == null) {
            throw new CurrenciesDoNotMatchException("Product currency does not match promo code currency");
        }
        if(promoCode.getExpirationDate().before(new Date())) {
            throw new PromoCodeExpiredException("Promo code is expired");
        }
        if(promoCode.getUsesLeft() <= 0) {
            throw new PromoCodeExpiredException("Promo code has no uses left");
        }

        double discountPrice = productPrice - promoCode.getDiscountAmount();
        if(discountPrice < 0) {
            return 0;
        }
        return discountPrice;
    }
}
