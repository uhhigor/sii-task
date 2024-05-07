package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.uhhigor.siitask.util.CurrencyConverter;

import java.util.Currency;

@Entity
@Getter
@Setter
public class ProductPrice {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Convert(converter = CurrencyConverter.class)
    @Column(nullable = false)
    private Currency currency;

    @Getter
    public static class ProductPriceDto {
        private Double price;
        private String currency;

        public ProductPriceDto(Double price, String currency) {
            this.price = price;
            this.currency = currency;
        }

        public ProductPriceDto(ProductPrice productPrice) {
            this.price = productPrice.getPrice();
            this.currency = productPrice.getCurrency().getCurrencyCode();
        }
    }
}
