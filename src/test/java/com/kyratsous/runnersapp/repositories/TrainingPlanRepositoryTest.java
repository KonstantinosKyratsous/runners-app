package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainingPlanRepositoryTest {

    @Autowired
    TrainingPlanRepository trainingPlanRepository;

    @Autowired
    UserRepository userRepository;

    User coach;

    TrainingPlan trainingPlan;

    TrainingPlan trainingPlan2;

    @BeforeEach
    void setUp() {
        coach = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"COACH"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(coach);

        trainingPlan = new TrainingPlan("Test Title", "Test Body", new Date(), coach, "Novice", new HashSet<>(), new HashSet<>());
        trainingPlanRepository.save(trainingPlan);

        trainingPlan2 = new TrainingPlan("Test Title 2", "Test Body 2", new Date(), coach, "Novice", new HashSet<>(), new HashSet<>());
        trainingPlanRepository.save(trainingPlan2);
    }

    @Test
    void findAllByCoach() {
        Set<TrainingPlan> trainingPlans = trainingPlanRepository.findAllByCoach(coach);

        assertEquals(2, trainingPlans.size());
    }

    @Test
    void findAllByDistanceOptionsIsIn() {
        Set<String> options = new HashSet<>();
        options.add("Marathon");

        Set<TrainingPlan> trainingPlans = trainingPlanRepository.findAllByDistanceOptionsIsIn(options);

        assertEquals(0, trainingPlans.size());
    }

    @Test
    void findAllByExperienceIsIn() {
        Set<String> options = new HashSet<>();
        options.add("Novice");

        Set<TrainingPlan> trainingPlans = trainingPlanRepository.findAllByExperienceIsIn(options);

        assertEquals(2, trainingPlans.size());
    }

    @Test
    void findAllByFieldOptionsIsIn() {
        Set<String> options = new HashSet<>();
        options.add("Track");

        Set<TrainingPlan> trainingPlans = trainingPlanRepository.findAllByFieldOptionsIsIn(options);

        assertEquals(0, trainingPlans.size());
    }
}