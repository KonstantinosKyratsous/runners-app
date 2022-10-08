package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RaceFavoriteRepositoryTest {

    @Autowired
    RaceFavoriteRepository raceFavoriteRepository;

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Race race;
    RaceFavorite favorite;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        race = new Race(user, "Test Title", "Test Description", new HashSet<>(), new HashSet<>(),
                0.0, 0.0, "Location", new Date(), 0.0, "test_link.com", new byte[0]);
        raceRepository.save(race);

        favorite = new RaceFavorite(race, user);
        raceFavoriteRepository.save(favorite);
    }

    @Test
    void existsRaceFavoriteByRaceAndUser() {
        assertTrue(raceFavoriteRepository.existsRaceFavoriteByRaceAndUser(race, user));
    }

    @Test
    void deleteRaceFavoriteByRaceAndUser() {
        raceFavoriteRepository.deleteRaceFavoriteByRaceAndUser(race, user);

        assertFalse(raceFavoriteRepository.existsRaceFavoriteByRaceAndUser(race, user));
    }
}