package org.uhhigor.siitask.service;


import org.springframework.stereotype.Service;
import org.uhhigor.siitask.builder.PromoCodeBuilder;
import org.uhhigor.siitask.exception.promocode.PromoCodeBuilderException;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.exception.promocode.PromoCodeServiceException;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.repository.PromoCodeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public List<PromoCode> getAllPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodeRepository.findAll().forEach(promoCodes::add);
        return promoCodes;
    }

    public PromoCode addPromoCode(PromoCode.PromoCodeDto promoCodeDto) throws PromoCodeServiceException {
        if(codeExists(promoCodeDto.getCode())) {
            throw new PromoCodeServiceException("Promo code with this code already exists");
        }
        try {
            PromoCode promoCode = new PromoCodeBuilder()
                    .code(promoCodeDto.getCode())
                    .expirationDate(promoCodeDto.getExpirationDate())
                    .discountAmount(promoCodeDto.getDiscountAmount())
                    .currency(promoCodeDto.getCurrency())
                    .uses(promoCodeDto.getUses())
                    .build();
            return promoCodeRepository.save(promoCode);
        } catch (PromoCodeBuilderException e) {
            throw new PromoCodeServiceException("Error while creating promo code: " + e.getMessage());
        }
    }

    public PromoCode getByCode(String code) throws PromoCodeNotFoundException {
        return promoCodeRepository.findByCode(code).orElseThrow(() -> new PromoCodeNotFoundException("Promo code not found"));
    }

    public boolean codeExists(String code) {
        return promoCodeRepository.existsByCode(code);
    }

    public void usePromoCode(PromoCode promoCode) {
        promoCode.setUsesLeft(promoCode.getUsesLeft() - 1);
        promoCodeRepository.save(promoCode);
    }
}
