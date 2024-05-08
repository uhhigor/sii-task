package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.exception.purchase.PurchaseServiceException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.model.Purchase;
import org.uhhigor.siitask.service.ProductService;
import org.uhhigor.siitask.service.PromoCodeService;
import org.uhhigor.siitask.service.PurchaseService;

import java.util.Currency;
import java.util.Date;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    private final ProductService productService;

    private final PromoCodeService promoCodeService;

    public PurchaseController(PurchaseService purchaseService, ProductService productService, PromoCodeService promoCodeService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.promoCodeService = promoCodeService;
    }
    @PostMapping("/finalize")
    public ResponseEntity<Object> finalizePurchase(@RequestBody PurchaseRequest purchaseRequest) {
        Product product;
        try {
            product = productService.getProductById(purchaseRequest.getProductId());
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        Currency currency;
        try {
            currency = Currency.getInstance(purchaseRequest.getCurrencyCode());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid currency code");
        }
        PromoCode promoCode = null;
        if(!purchaseRequest.getPromoCode().isEmpty()) {
            try {
                promoCode = promoCodeService.getByCode(purchaseRequest.getPromoCode());
            } catch (PromoCodeNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        }

        try {
            Purchase purchase;
            if(promoCode == null) {
                purchase = purchaseService.finalizePurchase(product, currency);
            } else {
                purchase = purchaseService.finalizePurchase(product, currency, promoCode);
                promoCodeService.usePromoCode(promoCode);
            }
            return ResponseEntity.ok(new PurchaseResponse(purchase));
        } catch (PurchaseServiceException | CurrenciesDoNotMatchException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Getter
    @NoArgsConstructor
    public static class PurchaseResponse {
        private Date date;
        private Long productId;
        private Double regularPrice;
        private Double discountApplied;
        private Double finalPrice;
        private String currencyCode;

        public PurchaseResponse(Purchase purchase) {
            this.date = purchase.getDate();
            this.productId = purchase.getProduct().getId();
            this.regularPrice = purchase.getRegularPrice();
            this.discountApplied = purchase.getDiscountApplied();
            this.finalPrice = purchase.getFinalPrice();
            this.currencyCode = purchase.getCurrency().getCurrencyCode();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PurchaseRequest {
        private Long productId;
        private String currencyCode;
        private String promoCode;
    }

}
