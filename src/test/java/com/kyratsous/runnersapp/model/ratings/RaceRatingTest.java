package com.kyratsous.runnersapp.model.ratings;

import com.kyratsous.runnersapp.model.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RaceRatingTest {

    static RaceRating rating;

    @BeforeEach
    void setUp() {
        rating = new RaceRating();
    }

    @Test
    void getRace() {
        Race race = new Race();

        rating.setRace(race);
        assertEquals(race, rating.getRace());
    }
}