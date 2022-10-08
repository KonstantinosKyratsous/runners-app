package com.kyratsous.runnersapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RaceTest {

    static Race race;

    @BeforeEach
    public void setUp() {
        race = new Race();
    }

    @Test
    public void getOrganizer() {
        User organizer = new User();

        race.setOrganizer(organizer);
        assertEquals(organizer, race.getOrganizer());
    }

    @Test
    public void getTitle() {
        String title = "Test title";

        race.setTitle(title);
        assertEquals(title, race.getTitle());
    }

    @Test
    public void getDescription() {
        String description = "Test Description";

        race.setDescription(description);
        assertEquals(description, race.getDescription());
    }

    @Test
    public void getDistanceOptions() {
        Set<String> options = new HashSet<>();
        options.add("Marathon");
        options.add("Half Marathon");

        race.setDistanceOptions(options);
        assertEquals(options, race.getDistanceOptions());
    }

    @Test
    public void getFieldOptions() {
        Set<String> options = new HashSet<>();
        options.add("Track");

        race.setFieldOptions(options);
        assertEquals(options, race.getFieldOptions());
    }

    @Test
    public void getLatitude() {
        double latitude = 38.706646;

        race.setLatitude(latitude);
        assertEquals(latitude, race.getLatitude());
    }

    @Test
    public void getLongitude() {
        double longitude = 20.64073;

        race.setLongitude(longitude);
        assertEquals(longitude, race.getLongitude());
    }

    @Test
    public void getLocation() {
        String location = "Ioannina";

        race.setLocation(location);
        assertEquals(location, race.getLocation());
    }

    @Test
    public void getDate() {
        Date date = new Date();

        race.setDate(date);
        assertEquals(date, race.getDate());
    }

    @Test
    public void getPrice() {
        double price = 12.5;

        race.setPrice(price);
        assertEquals(price, race.getPrice());
    }

    @Test
    public void getRegistrationLink() {
        String link = "www.test_link.com";

        race.setRegistrationLink(link);
        assertEquals(link, race.getRegistrationLink());
    }

    @Test
    public void getFile() {
        byte [] file = new byte[0];

        race.setFile(file);
        assertEquals(file, race.getFile());
    }
}