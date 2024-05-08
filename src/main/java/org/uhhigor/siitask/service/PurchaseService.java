package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.builder.PurchaseBuilder;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.purchase.PurchaseBuilderException;
import org.uhhigor.siitask.exception.purchase.PurchaseServiceException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.model.Purchase;
import org.uhhigor.siitask.repository.PurchaseRepository;

import java.util.Currency;
import java.util.Date;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase finalizePurchase(Product product, Currency currency) throws PurchaseServiceException {
        Double regularPrice = product.getProductPriceByCurrency(currency).getPrice();
        if(regularPrice == null) {
            throw new PurchaseServiceException("Product price in selected currency not found");
        }
        try {
            Purchase purchase = new PurchaseBuilder()
                    .product(product)
                    .currency(currency)
                    .date(new Date())
                    .build();
            return purchaseRepository.save(purchase);
        } catch (PurchaseBuilderException e) {
            throw new PurchaseServiceException("Error while creating purchase: " + e.getMessage(), e);
        }
    }

    public Purchase finalizePurchase(Product product, Currency currency, PromoCode promoCode) throws PurchaseServiceException, CurrenciesDoNotMatchException {
        if(!promoCode.getCurrency().equals(currency)) {
            throw new CurrenciesDoNotMatchException("Promo code currency does not match purchase currency");
        }
        Double regularPrice = product.getProductPriceByCurrency(currency).getPrice();
        if(regularPrice == null) {
            throw new PurchaseServiceException("Product price in selected currency not found");
        }
        try {
            Purchase purchase = new PurchaseBuilder()
                    .product(product)
                    .currency(currency)
                    .date(new Date())
                    .discount(promoCode.getDiscountAmount())
                    .regularPrice(regularPrice)
                    .build();
            return purchaseRepository.save(purchase);
        } catch (PurchaseBuilderException e) {
            throw new PurchaseServiceException("Error while creating purchase: " + e.getMessage(), e);
        }
    }
}
