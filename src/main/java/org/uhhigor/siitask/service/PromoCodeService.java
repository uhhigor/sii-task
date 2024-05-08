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
    private final ProductService productService;

    public PromoCodeService(PromoCodeRepository promoCodeRepository, ProductService productService) {
        this.promoCodeRepository = promoCodeRepository;
        this.productService = productService;
    }

    public List<PromoCode> getAllPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodeRepository.findAll().forEach(promoCodes::add);
        return promoCodes;
    }

    public PromoCode addPromoCode(PromoCode.PromoCodeDto promoCodeDto) throws PromoCodeServiceException {
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

    public void deletePromoCode(String code) throws PromoCodeNotFoundException {
        PromoCode promoCode = getByCode(code);
        promoCodeRepository.delete(promoCode);
    }

    public void usePromoCode(PromoCode promoCode) {
        promoCode.setUsesLeft(promoCode.getUsesLeft() - 1);
        promoCodeRepository.save(promoCode);
    }
}
