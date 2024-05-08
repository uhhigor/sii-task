package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.uhhigor.siitask.util.CurrencyConverter;

import java.util.Currency;
import java.util.Date;

@Entity
@Getter
@Setter
public class PromoCode {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(nullable = false)
    private Double discountAmount;

    @Convert(converter = CurrencyConverter.class)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private Integer usesLeft;

    @Getter
    public static class PromoCodeDto {
        private final String code;
        private final Date expirationDate;
        private final Double discountAmount;
        private final String currency;
        private final Integer uses;
        public PromoCodeDto(PromoCode promoCode) {
            this.code = promoCode.getCode();
            this.expirationDate = promoCode.getExpirationDate();
            this.discountAmount = promoCode.getDiscountAmount();
            this.currency = promoCode.getCurrency().getCurrencyCode();
            this.uses = promoCode.getUsesLeft();
        }
    }
}
