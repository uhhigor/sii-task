package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.exception.promocode.PromoCodeUsesInvalidException;
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
    public ResponseEntity<PurchaseResponse> finalizePurchase(@RequestBody PurchaseRequest purchaseRequest) {
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
            return ResponseEntity.badRequest().body(new PurchaseResponse("Error while finalizing purchase. Invalid currency code: " + purchaseRequest.getCurrencyCode()));
        }
        PromoCode promoCode = null;
        if(purchaseRequest.getPromoCode() != null) {
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
                promoCodeService.usePromoCode(promoCode);
                purchase = purchaseService.finalizePurchase(product, currency, promoCode);
            }
            return ResponseEntity.ok(new PurchaseResponse("Purchase successful", purchase));
        } catch (PurchaseServiceException | CurrenciesDoNotMatchException | PromoCodeUsesInvalidException e) {
            return ResponseEntity.badRequest().body(new PurchaseResponse(e.getMessage()));
        }
    }
    @Getter
    @NoArgsConstructor
    public static class PurchaseResponse {
        private String message;
        private PurchaseData purchase;

        @Getter
        @NoArgsConstructor
        private static class PurchaseData {
            private Date date;
            private Long productId;
            private Double regularPrice;
            private Double discountApplied;
            private String currencyCode;
        }

        public PurchaseResponse(String message) {
            this.message = message;
        }

        public PurchaseResponse(String message, Purchase purchase) {
            this.message = message;
            this.purchase = new PurchaseData();
            this.purchase.date = purchase.getDate();
            this.purchase.productId = purchase.getProduct().getId();
            this.purchase.regularPrice = purchase.getRegularPrice();
            this.purchase.discountApplied = purchase.getDiscountApplied();
            this.purchase.currencyCode = purchase.getCurrency().getCurrencyCode();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PurchaseRequest {
        private Long productId;
        private String currencyCode;
        private String promoCode;
    }

}
