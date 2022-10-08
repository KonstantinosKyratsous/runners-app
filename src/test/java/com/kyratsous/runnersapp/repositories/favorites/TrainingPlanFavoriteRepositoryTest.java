package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.TrainingPlanFavorite;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainingPlanFavoriteRepositoryTest {

    @Autowired
    TrainingPlanFavoriteRepository trainingPlanFavoriteRepository;

    @Autowired
    TrainingPlanRepository trainingPlanRepository;

    @Autowired
    UserRepository userRepository;

    TrainingPlanFavorite favorite;
    TrainingPlan trainingPlan;
    User user;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        trainingPlan = new TrainingPlan("Test Title", "Test Body", new Date(), user, "Novice", new HashSet<>(), new HashSet<>());
        trainingPlanRepository.save(trainingPlan);

        favorite = new TrainingPlanFavorite(trainingPlan, user);
        trainingPlanFavoriteRepository.save(favorite);
    }

    @Test
    void existsTrainingPlanFavoriteByTrainingPlanAndUser() {
        assertTrue(trainingPlanFavoriteRepository.existsTrainingPlanFavoriteByTrainingPlanAndUser(trainingPlan, user));
    }

    @Test
    void deleteTrainingPlanFavoriteByTrainingPlanAndUser() {
        trainingPlanFavoriteRepository.deleteTrainingPlanFavoriteByTrainingPlanAndUser(trainingPlan, user);

        assertFalse(trainingPlanFavoriteRepository.existsTrainingPlanFavoriteByTrainingPlanAndUser(trainingPlan, user));
    }
}