package com.kyratsous.runnersapp.model;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DietTest {

    static Diet diet;

    @BeforeEach
    public void setUp() {
        diet = new Diet();
    }

    @Test
    public void getTitle() {
        String title = "Title";

        diet.setTitle(title);
        assertEquals(title, diet.getTitle());
    }

    @Test
    public void getBody() {
        String body = "body";

        diet.setBody(body);
        assertEquals(body, diet.getBody());
    }

    @Test
    public void getDate() {
        Date date = new Date();

        diet.setDate(date);
        assertEquals(date, diet.getDate());
    }

    @Test
    public void getNutritionist() {
        User nutritionist = new User();

        diet.setNutritionist(nutritionist);
        assertEquals(nutritionist, diet.getNutritionist());
    }
}