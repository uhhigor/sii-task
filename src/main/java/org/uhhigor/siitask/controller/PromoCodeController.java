package org.uhhigor.siitask.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.exception.promocode.PromoCodeServiceException;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.service.PromoCodeService;


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
            return ResponseEntity.ok(new PromoCode.PromoCodeDto(promoCodeService.getByCode(code)));
        } catch (PromoCodeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> addPromoCode(@RequestBody PromoCode.PromoCodeDto promoCodeDto) {
        try {
            PromoCode promoCode = promoCodeService.addPromoCode(promoCodeDto);
            return ResponseEntity.ok(new PromoCode.PromoCodeDto(promoCode));
        } catch (PromoCodeServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
