package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.ProductRating;

import java.util.Set;

public interface ProductRatingService extends CrudService<ProductRating, Long> {

    Set<ProductRating> findAllByProductId(Long id);
}
