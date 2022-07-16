package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Product;

import java.util.Map;
import java.util.Set;

public interface ProductService extends CrudService<Product, Long> {

    Set<String> findProductTypes();

    Set<Product> findFilteredProducts(Map<String, String> filters);
}
