package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ProductRating;
import com.kyratsous.runnersapp.repositories.ratings.ProductRatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRatingServiceTest {

    @InjectMocks
    ProductRatingService service;

    @Mock
    ProductRatingRepository repository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        ProductRating rating1 = new ProductRating();
        ProductRating rating2 = new ProductRating();
        Set<ProductRating> ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        when(repository.findAll()).thenReturn(ratings);

        Set<ProductRating> ratingsAll = service.findAll();

        assertNotNull(ratingsAll);
        assertEquals(2, ratingsAll.size());

        verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new ProductRating()));
        service.findById(1L);

        verify(repository).findById(anyLong());
    }

    @Test
    void save() {
        when(repository.save(any())).thenReturn(new ProductRating());
        service.save(new ProductRating());

        verify(repository).save(any());
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setUsername("test");
        ProductRating rating = new ProductRating();
        rating.setUser(user);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.setContext(context);

        when(repository.findById(anyLong())).thenReturn(Optional.of(rating));
        service.deleteById(1L);

        verify(repository).findById(anyLong());
    }

    @Test
    void findAllByUser() {
        ProductRating rating1 = new ProductRating();
        ProductRating rating2 = new ProductRating();
        Set<ProductRating> ratingsByUser = new HashSet<>();
        ratingsByUser.add(rating1);
        ratingsByUser.add(rating2);

        when(repository.findAllByUser(any())).thenReturn(ratingsByUser);
        service.findAllByUser(new User());

        verify(repository).findAllByUser(any());
    }

    @Test
    void findAllByProductId() {
        ProductRating rating1 = new ProductRating();
        ProductRating rating2 = new ProductRating();
        Set<ProductRating> ratingsByProduct = new HashSet<>();
        ratingsByProduct.add(rating1);
        ratingsByProduct.add(rating2);

        when(repository.findAllByProductId(anyLong())).thenReturn(ratingsByProduct);
        service.findAllByProductId(1L);

        verify(repository).findAllByProductId(anyLong());
    }
}