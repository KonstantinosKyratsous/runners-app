package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.TrainingPlanFavorite;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface TrainingPlanFavoriteRepository extends CrudRepository<TrainingPlanFavorite, Long> {

    boolean existsTrainingPlanFavoriteByTrainingPlanAndUser(@NotNull TrainingPlan trainingPlan, @NotNull User user);

    void deleteTrainingPlanFavoriteByTrainingPlanAndUser(@NotNull TrainingPlan trainingPlan, @NotNull User user);
}
