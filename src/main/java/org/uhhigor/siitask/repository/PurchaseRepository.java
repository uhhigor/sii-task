package org.uhhigor.siitask.repository;

import org.springframework.data.repository.CrudRepository;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.Purchase;

public interface PurchaseRepository extends CrudRepository<Purchase, Long>{
}
