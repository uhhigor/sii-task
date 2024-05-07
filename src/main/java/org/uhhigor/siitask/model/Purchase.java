package org.uhhigor.siitask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue
    private Long id;


    @Column(nullable = false)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false)
    private Double regularPrice;

    private Double discountApplied;
}
