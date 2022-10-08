package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.DietFavorite;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DietFavoriteRepositoryTest {

    @Autowired
    DietFavoriteRepository dietFavoriteRepository;

    @Autowired
    DietRepository dietRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    Diet diet;

    Diet diet2;

    DietFavorite favorite;
    DietFavorite favorite2;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        diet = new Diet("Test Title", "Test Body", new Date(), user);
        dietRepository.save(diet);

        diet2 = new Diet("Test Title 2", "Test Body 2", new Date(), user);
        dietRepository.save(diet2);

        favorite = new DietFavorite(diet, user);
        dietFavoriteRepository.save(favorite);

        favorite2 = new DietFavorite(diet2, user);
        dietFavoriteRepository.save(favorite2);
    }

    @Test
    void existsDietFavoriteByDietAndUser() {
        assertTrue(dietFavoriteRepository.existsDietFavoriteByDietAndUser(diet, user));
    }

    @Test
    void deleteDietFavoriteByDietAndUser() {
        dietFavoriteRepository.deleteDietFavoriteByDietAndUser(diet, user);

        assertFalse(dietFavoriteRepository.existsDietFavoriteByDietAndUser(diet, user));
    }
}