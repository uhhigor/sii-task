package org.uhhigor.siitask.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.uhhigor.siitask.model.Purchase;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class SalesReportService {
    private final PurchaseService purchaseService;

    public SalesReportService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public SalesReport getSalesReport() {
        return new SalesReport(purchaseService.getAllPurchases());
    }

    @Getter
    public static class SalesReport {
        private final List<SalesReportEntry> entries;

        public SalesReport(List<Purchase> purchaseList) {
            Map<Currency, SalesReportEntry> salesReportMap = new HashMap<>();
            for (Purchase purchase : purchaseList) {
                Currency currency = purchase.getCurrency();
                SalesReportEntry salesReportEntry = salesReportMap.get(currency);
                if (salesReportEntry == null) {
                    salesReportEntry = new SalesReportEntry(currency, 0, 0, 0);
                    salesReportMap.put(currency, salesReportEntry);
                }
                salesReportEntry.setTotalAmount(salesReportEntry.getTotalAmount() + purchase.getRegularPrice());
                salesReportEntry.setTotalDiscount(salesReportEntry.getTotalDiscount() + purchase.getDiscountApplied());
                salesReportEntry.setNumberOfPurchases(salesReportEntry.getNumberOfPurchases() + 1);
            }
            entries = List.copyOf(salesReportMap.values());
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SalesReportEntry {
        private Currency currency;
        private double totalAmount;
        private double totalDiscount;
        private int numberOfPurchases;
    }
}
