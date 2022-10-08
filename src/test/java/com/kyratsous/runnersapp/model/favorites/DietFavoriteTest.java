package com.kyratsous.runnersapp.model.favorites;

import com.kyratsous.runnersapp.model.Diet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DietFavoriteTest {

    static DietFavorite favorite;

    @BeforeEach
    public void setUp() {
        favorite = new DietFavorite();
    }

    @Test
    public void getDiet() {
        Diet diet = new Diet();

        favorite.setDiet(diet);
        assertEquals(diet, favorite.getDiet());
    }
}