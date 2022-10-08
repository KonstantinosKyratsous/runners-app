package com.kyratsous.runnersapp.model.favorites;

import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FavoriteTest {

    static Favorite favorite;

    @BeforeEach
    public void setUp() {
        favorite = new Favorite();
    }

    @Test
    public void getUser() {
        User user = new User();

        favorite.setUser(user);
        assertEquals(user, favorite.getUser());
    }
}