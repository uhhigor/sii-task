package org.uhhigor.siitask.service;


import org.springframework.stereotype.Service;
import org.uhhigor.siitask.exception.promocode.PromoCodeNotFoundException;
import org.uhhigor.siitask.exception.promocode.PromoCodeServiceException;
import org.uhhigor.siitask.exception.promocode.PromoCodeUsesInvalidException;
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

    public PromoCode addPromoCode(PromoCode promoCode) throws PromoCodeServiceException {
        if(codeExists(promoCode.getCode())) {
            throw new PromoCodeServiceException("Promo code with this code already exists");
        }
            return promoCodeRepository.save(promoCode);
        }

    public PromoCode getByCode(String code) throws PromoCodeNotFoundException {
        return promoCodeRepository.findByCode(code).orElseThrow(() -> new PromoCodeNotFoundException("Promo code not found"));
    }

    public boolean codeExists(String code) {
        return promoCodeRepository.existsByCode(code);
    }

    public void usePromoCode(PromoCode promoCode) throws PromoCodeUsesInvalidException {
        if(promoCode.getUsesLeft() <= 0) {
            throw new PromoCodeUsesInvalidException("Error while using promo code: Promo code uses limit reached");
        }
        promoCode.setUsesLeft(promoCode.getUsesLeft() - 1);
        promoCode.setTimesUsed(promoCode.getTimesUsed() + 1);
        promoCodeRepository.save(promoCode);
    }
}
