package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.RaceRating;
import com.kyratsous.runnersapp.repositories.RaceRepository;
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
class RaceRatingRepositoryTest {

    @Autowired
    RaceRatingRepository raceRatingRepository;

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Race race;
    RaceRating rating;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        race = new Race(user, "Test Title", "Test Description", new HashSet<>(), new HashSet<>(),
                0.0, 0.0, "Location", new Date(), 0.0, "test_link.com", new byte[0]);

        raceRepository.save(race);

        rating = new RaceRating();
        rating.setRate(5);
        rating.setDescription("Test Description");
        rating.setUser(user);
        rating.setRace(race);
        raceRatingRepository.save(rating);
    }

    @Test
    void findAllByRaceId() {
        Set<RaceRating> ratings = raceRatingRepository.findAllByRaceId(race.getId());

        assertEquals(1, ratings.size());
    }

    @Test
    void findAllByUser() {
        Set<RaceRating> ratings = raceRatingRepository.findAllByUser(user);

        assertEquals(1, ratings.size());
    }
}