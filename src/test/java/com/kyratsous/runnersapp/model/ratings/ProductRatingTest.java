package com.kyratsous.runnersapp.model.ratings;

import com.kyratsous.runnersapp.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductRatingTest {

    static ProductRating rating;

    @BeforeEach
    void setUp() {
        rating = new ProductRating();
    }

    @Test
    void getProduct() {
        Product product = new Product();

        rating.setProduct(product);
        assertEquals(product, rating.getProduct());
    }
}