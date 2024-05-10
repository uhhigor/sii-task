package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.promocode.PromoCodeException;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.service.DiscountPriceService;
import org.uhhigor.siitask.service.ProductService;
import org.uhhigor.siitask.service.PromoCodeService;

import java.util.Currency;

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

    @PostMapping
    public ResponseEntity<DiscountPriceResponse> getDiscountPrice(@RequestBody DiscountPriceRequest discountPriceRequest) {
        PromoCode promoCode;
        try {
            promoCode = promoCodeService.getByCode(discountPriceRequest.getCode());
        } catch (PromoCodeNotFoundException e) {
            System.out.println("Promo code not found");
            return ResponseEntity.notFound().build();
        }
        Product product;
        try {
            product = productService.getProductById(discountPriceRequest.getProductId());
        } catch (ProductNotFoundException e) {
            System.out.println("Product not found");
            return ResponseEntity.notFound().build();
        }
        Currency currency = Currency.getInstance(discountPriceRequest.getCurrencyCode());
        ProductPrice productPrice = product.getProductPriceByCurrency(currency);
        try {
            double discountPrice = discountPriceService.getDiscountPrice(product, promoCode);
            return ResponseEntity.ok(new DiscountPriceResponse(discountPrice, productPrice.getCurrency()));
        } catch (PromoCodeException e) {
            return ResponseEntity.badRequest().body(new DiscountPriceResponse(e.getMessage(), productPrice.getPrice(), productPrice.getCurrency()));
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountPriceRequest {
        private Long productId;
        private String code;
        private String currencyCode;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DiscountPriceResponse {
        private String message;
        private DiscountPriceData discountPrice;

        public DiscountPriceResponse(double discountPrice, Currency currency) {
            this.discountPrice = new DiscountPriceData(discountPrice, currency.getCurrencyCode());
        }

        public DiscountPriceResponse(String message, double discountPrice, Currency currency) {
            this.message = message;
            this.discountPrice = new DiscountPriceData(discountPrice, currency.getCurrencyCode());
        }
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        static class DiscountPriceData {
            private double price;
            private String currency;
        }
    }
}
