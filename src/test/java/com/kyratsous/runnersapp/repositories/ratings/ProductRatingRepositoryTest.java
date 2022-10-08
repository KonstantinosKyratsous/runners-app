package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ProductRating;
import com.kyratsous.runnersapp.repositories.ProductRepository;
import com.kyratsous.runnersapp.repositories.UserRepository;
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
class ProductRatingRepositoryTest {

    @Autowired
    ProductRatingRepository productRatingRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Product product;
    ProductRating rating;

    @BeforeEach
    void setUp() {
        user = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"ORGANIZER"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(user);

        product = new Product("Test Title", "Shoes", "Marathon", 5.0, new byte[0], 123.0,
                "Test Description", new HashSet<>(), new HashSet<>(), new Date());
        product.setCoach(user);
        productRepository.save(product);

        rating = new ProductRating();
        rating.setRate(5);
        rating.setDescription("Test Description");
        rating.setUser(user);
        rating.setProduct(product);
        productRatingRepository.save(rating);
    }

    @Test
    void findAllByProductId() {
        Set<ProductRating> ratings = productRatingRepository.findAllByProductId(product.getId());

        assertEquals(1, ratings.size());
    }

    @Test
    void findAllByUser() {
        Set<ProductRating> ratings = productRatingRepository.findAllByUser(user);

        assertEquals(1, ratings.size());
    }
}