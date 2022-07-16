package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.DietRating;

import java.util.Set;

public interface DietRatingService extends CrudService<DietRating, Long> {

    Set<DietRating> findAllByDietId(Long id);
}
