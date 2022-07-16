package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.ExerciseRating;

import java.util.Set;

public interface ExerciseRatingService extends CrudService<ExerciseRating, Long>{

    Set<ExerciseRating> findAllByExerciseId(Long id);
}
