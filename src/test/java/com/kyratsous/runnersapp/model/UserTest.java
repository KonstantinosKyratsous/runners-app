package com.kyratsous.runnersapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    static User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void getFirstName() {
        String firstName = "John";

        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    public void getLastName() {
        String lastName = "Depp";

        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void getUsername() {
        String username = "john123";

        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void getEmail() {
        String email = "john@depp.com";

        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void getPassword() {
        String password = "john123";

        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void getEnabled() {
        int enabled = 1;

        user.setEnabled(enabled);
        assertEquals(enabled, user.getEnabled());
    }

    @Test
    public void getAuthorities() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ATHLETE");

        user.setAuthorities(authorities);
        assertEquals(authorities, user.getAuthorities());
    }

    @Test
    public void getDistancePrefs() {
        Set<String> distancePrefs = new HashSet<>();
        distancePrefs.add("Marathon");

        user.setDistancePrefs(distancePrefs);
        assertEquals(distancePrefs, user.getDistancePrefs());
    }

    @Test
    public void getExperience() {
        String experience = "Novice";

        user.setExperience(experience);
        assertEquals(experience, user.getExperience());
    }

    @Test
    public void getFieldTypes() {
        Set<String> fieldTypes = new HashSet<>();
        fieldTypes.add("Road");

        user.setFieldTypes(fieldTypes);
        assertEquals(fieldTypes, user.getFieldTypes());
    }
}