package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeExpiredException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;

import java.util.Date;

@Service
public class DiscountPriceService {
    public double getDiscountPrice(Product product, PromoCode promoCode) throws PromoCodeExpiredException, CurrenciesDoNotMatchException {
        ProductPrice productPrice = product.getProductPriceByCurrency(promoCode.getCurrency());
        if (productPrice == null) {
            throw new CurrenciesDoNotMatchException("Product currency does not match promo code currency");
        }
        double price = productPrice.getPrice();
        if(promoCode.getExpirationDate().before(new Date())) {
            throw new PromoCodeExpiredException("Promo code is expired");
        }
        if(promoCode.getUsesLeft() <= 0) {
            throw new PromoCodeExpiredException("Promo code has no uses left");
        }

        double discountPrice = price - promoCode.getDiscountAmount();
        if(discountPrice < 0) {
            return 0;
        }
        return discountPrice;
    }
}
