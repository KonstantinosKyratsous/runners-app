package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.DietRating;
import com.kyratsous.runnersapp.repositories.ratings.DietRatingRepository;
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
class DietRatingServiceTest {

    @InjectMocks
    DietRatingService service;

    @Mock
    DietRatingRepository repository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        DietRating rating1 = new DietRating();
        DietRating rating2 = new DietRating();
        Set<DietRating> ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        when(repository.findAll()).thenReturn(ratings);

        Set<DietRating> ratingsAll = service.findAll();

        assertNotNull(ratingsAll);
        assertEquals(2, ratingsAll.size());

        verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new DietRating()));
        service.findById(1L);

        verify(repository).findById(anyLong());
    }

    @Test
    void save() {
        when(repository.save(any())).thenReturn(new DietRating());
        service.save(new DietRating());

        verify(repository).save(any());
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setUsername("test");
        DietRating rating = new DietRating();
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
        DietRating rating1 = new DietRating();
        DietRating rating2 = new DietRating();
        Set<DietRating> ratingsByUser = new HashSet<>();
        ratingsByUser.add(rating1);
        ratingsByUser.add(rating2);

        when(repository.findAllByUser(any())).thenReturn(ratingsByUser);
        service.findAllByUser(new User());

        verify(repository).findAllByUser(any());
    }

    @Test
    void findAllByDietId() {
        DietRating rating1 = new DietRating();
        DietRating rating2 = new DietRating();
        Set<DietRating> ratingsByDiet = new HashSet<>();
        ratingsByDiet.add(rating1);
        ratingsByDiet.add(rating2);

        when(repository.findAllByDietId(anyLong())).thenReturn(ratingsByDiet);
        service.findAllByDietId(1L);

        verify(repository).findAllByDietId(anyLong());
    }
}