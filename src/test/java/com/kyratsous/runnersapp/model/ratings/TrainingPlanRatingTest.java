package com.kyratsous.runnersapp.model.ratings;

import com.kyratsous.runnersapp.model.TrainingPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingPlanRatingTest {

    static TrainingPlanRating rating;

    @BeforeEach
    void setUp() {
        rating = new TrainingPlanRating();
    }

    @Test
    void getTrainingPlan() {
        TrainingPlan trainingPlan = new TrainingPlan();

        rating.setTrainingPlan(trainingPlan);
        assertEquals(trainingPlan, rating.getTrainingPlan());
    }
}