package org.uhhigor.siitask.repository;

import org.springframework.data.repository.CrudRepository;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Long>{
}
