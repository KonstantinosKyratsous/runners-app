package com.kyratsous.runnersapp.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingPlanTest {

    static TrainingPlan trainingPlan;

    @BeforeEach
    public void setUp() {
        trainingPlan = new TrainingPlan();
    }

    @Test
    public void getTitle() {
        String title = "Title";

        trainingPlan.setTitle(title);
        assertEquals(title, trainingPlan.getTitle());
    }

    @Test
    public void getBody() {
        String body = "body";

        trainingPlan.setBody(body);
        assertEquals(body, trainingPlan.getBody());
    }

    @Test
    public void getDate() {
        Date date = new Date();

        trainingPlan.setDate(date);
        assertEquals(date, trainingPlan.getDate());
    }

    @Test
    public void getCoach() {
        User coach = new User();

        trainingPlan.setCoach(coach);
        assertEquals(coach, trainingPlan.getCoach());
    }

    @Test
    public void getExperience() {
        String experience = "Novice";

        trainingPlan.setExperience(experience);
        assertEquals(experience, trainingPlan.getExperience());
    }

    @Test
    public void getDistanceOptions() {
        Set<String> options = new HashSet<>();
        options.add("Marathon");

        trainingPlan.setDistanceOptions(options);
        assertEquals(options, trainingPlan.getDistanceOptions());
    }

    @Test
    public void getFieldOptions() {
        Set<String> options = new HashSet<>();
        options.add("Track");

        trainingPlan.setFieldOptions(options);
        assertEquals(options, trainingPlan.getFieldOptions());
    }
}