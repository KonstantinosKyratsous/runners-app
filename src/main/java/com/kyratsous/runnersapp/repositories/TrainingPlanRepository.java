package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TrainingPlanRepository extends CrudRepository<TrainingPlan, Long> {

    Set<TrainingPlan> findAllByCoach(User user);

    Set<TrainingPlan> findAllByDistanceOptionsIsIn(Set<String> distanceOptions);

    Set<TrainingPlan> findAllByExperienceIsIn(Set<String> experiences);

    Set<TrainingPlan> findAllByFieldOptionsIsIn(Set<String> fieldOptions);
}
