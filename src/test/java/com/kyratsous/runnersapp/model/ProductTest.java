package com.kyratsous.runnersapp.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    static Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
    }

    @Test
    public void getName() {
        String name = "Test Name";

        product.setName(name);
        assertEquals(name, product.getName());
    }

    @Test
    public void getCategory() {
        String category = "Shoes";

        product.setCategory(category);
        assertEquals(category, product.getCategory());
    }

    @Test
    public void getType() {
        String type = "Marathon";

        product.setType(type);
        assertEquals(type, product.getType());
    }

    @Test
    public void getRate() {
        double rate = 3.5;

        product.setRate(rate);
        assertEquals(rate, product.getRate());
    }

    @Test
    public void getImage() {
        byte[] image = new byte[0];

        product.setImage(image);
        assertEquals(image, product.getImage());
    }

    @Test
    public void getPrice() {
        double price = 12.5;

        product.setPrice(price);
        assertEquals(price, product.getPrice());
    }

    @Test
    public void getDescription() {
        String description = "test description";

        product.setDescription(description);
        assertEquals(description, product.getDescription());
    }

    @Test
    public void getPros() {
        Set<String> pros = new HashSet<>();
        pros.add("Good Product");

        product.setPros(pros);
        assertEquals(pros, product.getPros());
    }

    @Test
    public void getCons() {
        Set<String> cons = new HashSet<>();
        cons.add("Bad Product");

        product.setCons(cons);
        assertEquals(cons, product.getCons());
    }

    @Test
    public void getDate() {
        Date date = new Date();

        product.setDate(date);
        assertEquals(date, product.getDate());
    }

    @Test
    public void getCoach() {
        User coach = new User();

        product.setCoach(coach);
        assertEquals(coach, product.getCoach());
    }
}