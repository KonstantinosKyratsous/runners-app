package com.kyratsous.runnersapp.model.ratings;

import com.kyratsous.runnersapp.model.Diet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DietRatingTest {

    static DietRating rating;

    @BeforeEach
    void setUp() {
        rating = new DietRating();
    }

    @Test
    void getDiet() {
        Diet diet = new Diet();

        rating.setDiet(diet);
        assertEquals(diet, rating.getDiet());
    }
}