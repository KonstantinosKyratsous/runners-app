package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Exercise;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}
