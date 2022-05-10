package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
