package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ExerciseRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ExerciseRatingRepository extends CrudRepository<ExerciseRating, Long> {
    Set<ExerciseRating> findAllByExerciseId(Long id);

    Set<ExerciseRating> findAllByUser(User user);
}
