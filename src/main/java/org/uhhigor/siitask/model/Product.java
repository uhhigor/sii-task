package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
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
        for(ProductPrice price : prices) {
            if(price.getCurrency().equals(currency)) {
                return price;
            }
        }
        return null;
    }
    @Getter
    public static class ProductDto {
        private final String name;
        private final String description;
        private final List<ProductPrice.ProductPriceDto> prices;
        public ProductDto(Product product) {
            this.name = product.getName();
            this.description = product.getDescription();
            this.prices = new ArrayList<>();
            for(ProductPrice price : product.getPrices()) {
                this.prices.add(new ProductPrice.ProductPriceDto(price));
            }
        }
    }
}
