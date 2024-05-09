package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @NoArgsConstructor
    public static class PromoCodeDto {
        private String code;
        private Date expirationDate;
        private Double discountAmount;
        private String currency;
        private Integer uses;
    }
}
