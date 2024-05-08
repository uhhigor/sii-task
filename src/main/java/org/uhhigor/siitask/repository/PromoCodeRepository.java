package org.uhhigor.siitask.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.uhhigor.siitask.model.PromoCode;

import java.util.Optional;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Long> {
    Optional<PromoCode> findByCode(String code);
}
