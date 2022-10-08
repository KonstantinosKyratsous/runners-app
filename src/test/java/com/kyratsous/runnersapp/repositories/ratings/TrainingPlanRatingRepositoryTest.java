package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.TrainingPlanRating;
import com.kyratsous.runnersapp.repositories.TrainingPlanRepository;
import com.kyratsous.runnersapp.repositories.UserRepository;
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
class TrainingPlanRatingRepositoryTest {

    @Autowired
    TrainingPlanRatingRepository trainingPlanRatingRepository;

    @Autowired
    TrainingPlanRepository trainingPlanRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    TrainingPlan trainingPlan;
    TrainingPlanRating rating;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        trainingPlan = new TrainingPlan("Test Title", "Test Body", new Date(), user, "Novice", new HashSet<>(), new HashSet<>());
        trainingPlanRepository.save(trainingPlan);

        rating = new TrainingPlanRating();
        rating.setRate(5);
        rating.setDescription("Test Description");
        rating.setUser(user);
        rating.setTrainingPlan(trainingPlan);
        trainingPlanRatingRepository.save(rating);
    }

    @Test
    void findAllByTrainingPlanId() {
        Set<TrainingPlanRating> ratings = trainingPlanRatingRepository.findAllByTrainingPlanId(trainingPlan.getId());

        assertEquals(1, ratings.size());
    }

    @Test
    void findAllByUser() {
        Set<TrainingPlanRating> ratings = trainingPlanRatingRepository.findAllByUser(user);

        assertEquals(1, ratings.size());
    }
}