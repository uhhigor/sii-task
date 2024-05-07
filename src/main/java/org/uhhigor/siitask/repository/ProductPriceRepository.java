package org.uhhigor.siitask.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.uhhigor.siitask.model.ProductPrice;

public interface ProductPriceRepository extends CrudRepository<ProductPrice, Long> {
}
