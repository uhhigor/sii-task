package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;
import java.util.Date;
import java.util.List;

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

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Product> eligibleProducts;
}
