package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.exception.promocode.PromoCodeServiceException;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.service.PromoCodeService;

import java.util.Date;


@RestController
@RequestMapping("/promo-code")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping
    public ResponseEntity<Object> getPromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllPromoCodes());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> getPromoCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(new PromoCodeResponse(promoCodeService.getByCode(code)));
        } catch (PromoCodeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> addPromoCode(@RequestBody PromoCodeRequest promoCodeRequest) {
        try {
            PromoCode promoCode = promoCodeService.addPromoCode(
                    promoCodeRequest.getCode(),
                    promoCodeRequest.getExpirationDate(),
                    promoCodeRequest.getDiscountAmount(),
                    promoCodeRequest.getCurrency(),
                    promoCodeRequest.getUses()
            );
            return ResponseEntity.ok(new PromoCodeResponse("Promo code added successfully", promoCode));
        } catch (PromoCodeServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromoCodeRequest {
        private String code;
        private Date expirationDate;
        private Double discountAmount;
        private String currency;
        private Integer uses;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromoCodeResponse {
        private String message;
        private PromoCodeData promoCode;

        public PromoCodeResponse(String message, PromoCode promoCode) {
            this.message = message;
            this.promoCode = new PromoCodeData(promoCode);
        }

        public PromoCodeResponse(PromoCode promoCode) {
            this.promoCode = new PromoCodeData(promoCode);
        }

        static class PromoCodeData {
            private String code;
            private Date expirationDate;
            private Double discountAmount;
            private String currency;
            private Integer usesLeft;

            private Integer timesUsed;

            public PromoCodeData(PromoCode promoCode) {
                this.code = promoCode.getCode();
                this.expirationDate = promoCode.getExpirationDate();
                this.discountAmount = promoCode.getDiscountAmount();
                this.currency = promoCode.getCurrency().getCurrencyCode();
                this.usesLeft = promoCode.getUsesLeft();
                this.timesUsed = promoCode.getTimesUsed();
            }
        }
    }
}
