package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.DietRating;
import com.kyratsous.runnersapp.repositories.DietRepository;
import com.kyratsous.runnersapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DietRatingRepositoryTest {

    @Autowired
    DietRatingRepository dietRatingRepository;

    @Autowired
    DietRepository dietRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Diet diet;
    DietRating rating;
    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        diet = new Diet("Test Title", "Test Body", new Date(), user);
        dietRepository.save(diet);

        rating = new DietRating();
        rating.setRate(5);
        rating.setDescription("Test Description");
        rating.setUser(user);
        rating.setDiet(diet);
        dietRatingRepository.save(rating);
    }

    @Test
    void findAllByDietId() {
        Set<DietRating> ratings = dietRatingRepository.findAllByDietId(diet.getId());

        assertEquals(1, ratings.size());
    }

    @Test
    void findAllByUser() {
        Set<DietRating> ratings = dietRatingRepository.findAllByUser(user);

        assertEquals(1, ratings.size());
    }
}