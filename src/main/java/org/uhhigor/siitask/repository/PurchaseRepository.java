package org.uhhigor.siitask.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.uhhigor.siitask.model.Purchase;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
}
