package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    @Getter
    public static class ProductDto {
        private String name;
        private String description;
        private List<ProductPrice.ProductPriceDto> prices;

        public ProductDto(String name, String description, List<ProductPrice.ProductPriceDto> prices) {
            this.name = name;
            this.description = description;
            this.prices = prices;
        }
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
