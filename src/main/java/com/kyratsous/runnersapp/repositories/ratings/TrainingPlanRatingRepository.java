package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.TrainingPlanRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TrainingPlanRatingRepository extends CrudRepository<TrainingPlanRating, Long> {
    Set<TrainingPlanRating> findAllByTrainingPlanId(Long id);

    Set<TrainingPlanRating> findAllByUser(User user);
}
