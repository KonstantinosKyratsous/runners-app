package com.kyratsous.runnersapp.model.ratings;

import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RatingTest {

    static Rating rating;

    @BeforeEach
    void setUp() {
        rating = new Rating();
    }

    @Test
    void getRate() {
        int rate = 4;

        rating.setRate(rate);
        assertEquals(rate, rating.getRate());
    }

    @Test
    void getDescription() {
        String description = "Test description";

        rating.setDescription(description);
        assertEquals(description, rating.getDescription());
    }

    @Test
    void getUser() {
        User user = new User();

        rating.setUser(user);
        assertEquals(user, rating.getUser());
    }
}