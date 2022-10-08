package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RaceRepositoryTest {

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    UserRepository userRepository;

    Race race;

    Race race2;

    User organizer;

    @BeforeEach
    void setUp() {
        organizer = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);

        userRepository.save(organizer);

        race = new Race(organizer, "Test Title", "Test Description", new HashSet<>(), new HashSet<>(),
                0.0, 0.0, "Location", new Date(), 0.0, "test_link.com", new byte[0]);

        raceRepository.save(race);

        race2 = new Race(organizer, "Test Title 2", "Test Description 2", new HashSet<>(), new HashSet<>(),
                0.0, 0.0, "Location", new Date(), 0.0, "test_link.com", new byte[0]);

        raceRepository.save(race2);
    }

    @Test
    void findAllByOrganizer() {
        Set<Race> races = raceRepository.findAllByOrganizer(organizer);

        assertEquals(2, races.size());
    }

    @Test
    void findAllByOrderByDateAsc() {
        Set<Race> races = raceRepository.findAllByOrderByDateAsc();

        List<Date> dates = new ArrayList<>();
        for (Race tempRace: races) {
            dates.add(tempRace.getDate());
        }

        assertEquals(2, races.size());
        assertTrue(dates.get(0).before(dates.get(1)));
    }

    @Test
    void findAllByOrderByDateDesc() {
        Set<Race> races = raceRepository.findAllByOrderByDateDesc();

        List<Date> dates = new ArrayList<>();
        for (Race tempRace: races) {
            dates.add(tempRace.getDate());
        }

        assertEquals(2, races.size());
        assertTrue(dates.get(0).after(dates.get(1)));
    }

    @Test
    void findAllByDateBetween() {
        Set<Race> races = raceRepository.findAllByDateBetween(race.getDate(), race2.getDate());

        assertEquals(2, races.size());
    }

    @Test
    void findAllByDistanceOptionsIsIn() {
        Set<String> options = new HashSet<>();
        options.add("Marathon");

        Set<Race> races = raceRepository.findAllByDistanceOptionsIsIn(options);

        assertEquals(0, races.size());
    }

    @Test
    void findAllByFieldOptionsIsIn() {
        Set<String> options = new HashSet<>();
        options.add("Track");

        Set<Race> races = raceRepository.findAllByFieldOptionsIsIn(options);

        assertEquals(0, races.size());
    }
}