package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DietRepositoryTest {

    @Autowired
    DietRepository dietRepository;

    @Autowired
    UserRepository userRepository;

    User nutritionist;

    Diet diet;

    Diet diet2;

    @BeforeEach
    void setUp() {
        nutritionist = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"NUTRITIONIST"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(nutritionist);

        diet = new Diet("Test Title", "Test Body", new Date(), nutritionist);
        dietRepository.save(diet);

        diet2 = new Diet("Test Title 2", "Test Body 2", new Date(), nutritionist);
        dietRepository.save(diet2);
    }

    @Test
    void findAllByNutritionist() {
        Set<Diet> diets = dietRepository.findAllByNutritionist(nutritionist);

        assertEquals(2, diets.size());
    }
}