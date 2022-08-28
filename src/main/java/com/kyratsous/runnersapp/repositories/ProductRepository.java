package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Set<Product> findAllByCoach(User user);

    Set<Product> findAllByTypeIsIn(Set<String> types);

    Set<Product> findAllByCategoryIsIn(Set<String> categories);

    Set<Product> findAllByPriceBetween(double min, double max);
}
