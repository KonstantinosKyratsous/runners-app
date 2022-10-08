package com.kyratsous.runnersapp.model.favorites;

import com.kyratsous.runnersapp.model.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RaceFavoriteTest {

    static RaceFavorite favorite;

    @BeforeEach
    void setUp() {
        favorite = new RaceFavorite();
    }

    @Test
    void getRace() {
        Race race = new Race();

        favorite.setRace(race);
        assertEquals(race, favorite.getRace());
    }
}