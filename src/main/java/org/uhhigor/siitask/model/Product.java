package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductPrice> prices;

    public ProductPrice getProductPriceByCurrency(Currency currency) {
        for (ProductPrice price : prices) {
            if (price.getCurrency().equals(currency)) {
                return price;
            }
        }
        return null;
    }
}
