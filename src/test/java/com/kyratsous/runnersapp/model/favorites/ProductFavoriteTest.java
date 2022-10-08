package com.kyratsous.runnersapp.model.favorites;

import com.kyratsous.runnersapp.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductFavoriteTest {

    static ProductFavorite favorite;

    @BeforeEach
    void setUp() {
        favorite = new ProductFavorite();
    }

    @Test
    void getProduct() {
        Product product = new Product();

        favorite.setProduct(product);
        assertEquals(product, favorite.getProduct());
    }
}