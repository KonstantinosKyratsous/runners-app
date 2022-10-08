package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    User coach;

    Product product;

    Product product2;

    @BeforeEach
    void setUp() {
        coach = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"COACH"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(coach);

        product = new Product("Test Title", "Shoes", "Marathon", 5.0, new byte[0], 123.0,
                "Test Description", new HashSet<>(), new HashSet<>(), new Date());
        product.setCoach(coach);
        productRepository.save(product);

        product2 = new Product("Test Title 2", "Shoes", "Marathon", 4.0, new byte[0], 111.0,
                "Test Description 2", new HashSet<>(), new HashSet<>(), new Date());
        product2.setCoach(coach);
        productRepository.save(product2);
    }

    @Test
    void findAllByCoach() {
        Set<Product> products = productRepository.findAllByCoach(coach);

        assertEquals(2, products.size());
    }

    @Test
    void findAllByTypeIsIn() {
        Set<String> types = new HashSet<>();
        types.add("Marathon");

        Set<Product> products = productRepository.findAllByTypeIsIn(types);

        assertEquals(2, products.size());
    }

    @Test
    void findAllByCategoryIsIn() {
        Set<String> categories = new HashSet<>();
        categories.add("Shoes");

        Set<Product> products = productRepository.findAllByCategoryIsIn(categories);

        assertEquals(2, products.size());
    }

    @Test
    void findAllByPriceBetween() {
        Set<Product> products = productRepository.findAllByPriceBetween(100, 120);

        assertEquals(1, products.size());
    }

    @Test
    void findAllByRateBetween() {
        Set<Product> products = productRepository.findAllByRateBetween(4, 4.5);

        assertEquals(1, products.size());
    }

    @Test
    void findAllByOrderByRateAsc() {
        Set<Product> products = productRepository.findAllByOrderByRateAsc();

        assertEquals(product2, products.stream().findFirst().get());
    }

    @Test
    void findAllByOrderByRateDesc() {
        Set<Product> products = productRepository.findAllByOrderByRateDesc();

        assertEquals(product, products.stream().findFirst().get());
    }
}