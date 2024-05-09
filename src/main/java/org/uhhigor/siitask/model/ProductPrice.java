package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
}
