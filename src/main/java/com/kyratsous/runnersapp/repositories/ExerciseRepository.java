package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

    Set<Exercise> findAllByCoach(User user);

    Set<Exercise> findAllByDistanceOptionsIsIn(Set<String> distanceOptions);

    Set<Exercise> findAllByExperienceIsIn(Set<String> experiences);

    Set<Exercise> findAllByFieldOptionsIsIn(Set<String> fieldOptions);
}
