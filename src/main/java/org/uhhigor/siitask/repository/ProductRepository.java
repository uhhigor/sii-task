package org.uhhigor.siitask.repository;

import org.springframework.data.repository.CrudRepository;
import org.uhhigor.siitask.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{
}
