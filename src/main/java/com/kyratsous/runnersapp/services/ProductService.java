package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Product;

import java.util.Set;

public interface ProductService extends CrudService<Product, Long> {

    Set<String> findProductTypes();
}
