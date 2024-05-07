package org.uhhigor.siitask.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.uhhigor.siitask.model.PromoCode;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Long> {
}
