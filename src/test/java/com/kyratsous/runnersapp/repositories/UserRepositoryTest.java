package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ATHLETE"}).collect(Collectors.toSet()), null, "Novice", null);

        userRepository.save(user);
    }

    @Test
    void findUserByUsername() {
        User userRep = userRepository.findUserByUsername("username");

        assertEquals(userRep.getUsername(), user.getUsername());
    }
}