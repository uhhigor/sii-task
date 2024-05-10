package org.uhhigor.siitask.service;

import org.springframework.stereotype.Service;
import org.uhhigor.siitask.builder.PurchaseBuilder;
import org.uhhigor.siitask.exception.promocode.CurrenciesDoNotMatchException;
import org.uhhigor.siitask.exception.promocode.PromoCodeException;
import org.uhhigor.siitask.exception.purchase.PurchaseException;
import org.uhhigor.siitask.exception.purchase.PurchaseNotFoundException;
import org.uhhigor.siitask.exception.purchase.PurchaseServiceException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.model.Purchase;
import org.uhhigor.siitask.repository.PurchaseRepository;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final DiscountPriceService discountPriceService;

    private final PromoCodeService promoCodeService;

    public PurchaseService(PurchaseRepository purchaseRepository, DiscountPriceService discountPriceService, PromoCodeService promoCodeService) {
        this.purchaseRepository = purchaseRepository;
        this.discountPriceService = discountPriceService;
        this.promoCodeService = promoCodeService;
    }

    public Purchase finalizePurchase(Product product, Currency currency) throws PurchaseServiceException {
        try {
            Purchase purchase = new PurchaseBuilder()
                    .product(product)
                    .currency(currency)
                    .date(new Date())
                    .build();
            return purchaseRepository.save(purchase);
        } catch (PurchaseException e) {
            throw new PurchaseServiceException("Error while finalizing purchase: " + e.getMessage());
        }
    }

    public Purchase finalizePurchase(Product product, PromoCode promoCode) throws PurchaseServiceException, CurrenciesDoNotMatchException {
        try {
            double discount = discountPriceService.getDiscount(product, promoCode);

            Purchase purchase = new PurchaseBuilder()
                    .product(product)
                    .currency(promoCode.getCurrency())
                    .date(new Date())
                    .discount(discount)
                    .build();

            promoCodeService.usePromoCode(promoCode);
            return purchaseRepository.save(purchase);
        } catch (PurchaseException | PromoCodeException e) {
            throw new PurchaseServiceException("Error while finalizing purchase: " + e.getMessage());
        }
    }
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        purchaseRepository.findAll().forEach(purchases::add);
        return purchases;
    }
}
