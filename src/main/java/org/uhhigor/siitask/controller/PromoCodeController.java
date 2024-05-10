package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.builder.PromoCodeBuilder;
import org.uhhigor.siitask.exception.promocode.PromoCodeException;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.service.PromoCodeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/promo-code")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping
    public ResponseEntity<PromoCodeResponse> getPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(new PromoCodeResponse(promoCodes.size() + " promo codes found", promoCodes));
    }

    @GetMapping("/{code}")
    public ResponseEntity<PromoCodeResponse> getPromoCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(new PromoCodeResponse(List.of(promoCodeService.getByCode(code))));
        } catch (PromoCodeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PromoCodeResponse> addPromoCode(@RequestBody PromoCodeRequest promoCodeRequest) {
        try {
            PromoCode promoCode = new PromoCodeBuilder()
                    .code(promoCodeRequest.getCode())
                    .expirationDate(promoCodeRequest.getExpirationDate())
                    .discountAmount(promoCodeRequest.getDiscountAmount())
                    .currency(promoCodeRequest.getCurrency())
                    .uses(promoCodeRequest.getUses())
                    .type(promoCodeRequest.getType())
                    .build();
            promoCode = promoCodeService.addPromoCode(promoCode);
            return ResponseEntity.ok(new PromoCodeResponse("Promo code added successfully", List.of(promoCode)));
        } catch (PromoCodeException e) {
            return ResponseEntity.badRequest().body(new PromoCodeResponse("Error while creating promo code: " + e.getMessage()));
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
        private String type;
    }

    @Getter
    @NoArgsConstructor
    public static class PromoCodeResponse {
        private String message;
        private List<PromoCodeData> promoCodes;

        public PromoCodeResponse(String message, List<PromoCode> promoCodes) {
            this.message = message;
            this.promoCodes = new ArrayList<>();
            for (PromoCode promoCode : promoCodes) {
                this.promoCodes.add(new PromoCodeData(promoCode));
            }
        }

        public PromoCodeResponse(List<PromoCode> promoCodes) {
            this.promoCodes = new ArrayList<>();
            for (PromoCode promoCode : promoCodes) {
                this.promoCodes.add(new PromoCodeData(promoCode));
            }
        }

        public PromoCodeResponse(String message) {
            this.message = message;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        static class PromoCodeData {
            private String code;
            private Date expirationDate;
            private Double discountAmount;
            private String currency;
            private Integer usesLeft;
            private Integer timesUsed;
            private String type;

            public PromoCodeData(PromoCode promoCode) {
                this.code = promoCode.getCode();
                this.expirationDate = promoCode.getExpirationDate();
                this.discountAmount = promoCode.getDiscountAmount();
                this.currency = promoCode.getCurrency().getCurrencyCode();
                this.usesLeft = promoCode.getUsesLeft();
                this.timesUsed = promoCode.getTimesUsed();
                this.type = promoCode.getType().name();
            }
        }
    }
}
