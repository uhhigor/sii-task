package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeExpiredException;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.service.DiscountPriceService;
import org.uhhigor.siitask.service.ProductService;
import org.uhhigor.siitask.service.PromoCodeService;

@Controller
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountPriceService discountPriceService;

    private final PromoCodeService promoCodeService;

    private final ProductService productService;

    public DiscountController(DiscountPriceService discountPriceService, PromoCodeService promoCodeService, ProductService productService) {
        this.discountPriceService = discountPriceService;
        this.promoCodeService = promoCodeService;
        this.productService = productService;
    }

    @GetMapping("/product/{productId}/promo-code/{code}")
    public ResponseEntity<Object> pgetDiscountPrice(@PathVariable Long productId, @PathVariable String code) {

        PromoCode promoCode;
        try {
            promoCode = promoCodeService.getByCode(code);
        } catch (PromoCodeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        Product product;
        try {
            product = productService.getProductById(productId);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        try {
            double discountPrice = discountPriceService.getDiscountPrice(product, promoCode);
            return ResponseEntity.ok(new DiscountPriceResponse(discountPrice, promoCode.getCurrency().getCurrencyCode(), null));
        } catch (PromoCodeExpiredException | CurrenciesDoNotMatchException e) {
            double regularPrice = product.getProductPriceByCurrency(promoCode.getCurrency()).getPrice();
            return ResponseEntity.ok(new DiscountPriceResponse(regularPrice, promoCode.getCurrency().getCurrencyCode(), e.getMessage()));
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class DiscountPriceResponse {
        private double discountPrice;

        private String currency;

        private String warning;
    }
}
