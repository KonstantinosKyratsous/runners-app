package com.kyratsous.runnersapp.model.favorites;

import com.kyratsous.runnersapp.model.TrainingPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingPlanFavoriteTest {

    static TrainingPlanFavorite favorite;

    @BeforeEach
    public void setUp() {
        favorite = new TrainingPlanFavorite();
    }

    @Test
    public void getTrainingPlan() {
        TrainingPlan trainingPlan = new TrainingPlan();

        favorite.setTrainingPlan(trainingPlan);
        assertEquals(trainingPlan, favorite.getTrainingPlan());
    }
}