package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ExerciseFavorite;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface ExerciseFavoriteRepository extends CrudRepository<ExerciseFavorite, Long> {

    boolean existsRaceFavoriteByExerciseAndUser(@NotNull Exercise exercise, @NotNull User user);

    void deleteRaceFavoriteByExerciseAndUser(@NotNull Exercise exercise, @NotNull User user);
}
