package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.RaceRating;
import com.kyratsous.runnersapp.repositories.ratings.RaceRatingRepository;
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
class RaceRatingServiceTest {

    @InjectMocks
    RaceRatingService service;

    @Mock
    RaceRatingRepository repository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        RaceRating rating1 = new RaceRating();
        RaceRating rating2 = new RaceRating();
        Set<RaceRating> ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        when(repository.findAll()).thenReturn(ratings);

        Set<RaceRating> ratingsAll = service.findAll();

        assertNotNull(ratingsAll);
        assertEquals(2, ratingsAll.size());

        verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new RaceRating()));
        service.findById(1L);

        verify(repository).findById(anyLong());
    }

    @Test
    void save() {
        when(repository.save(any())).thenReturn(new RaceRating());
        service.save(new RaceRating());

        verify(repository).save(any());
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setUsername("test");
        RaceRating rating = new RaceRating();
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
        RaceRating rating1 = new RaceRating();
        RaceRating rating2 = new RaceRating();
        Set<RaceRating> ratingsByUser = new HashSet<>();
        ratingsByUser.add(rating1);
        ratingsByUser.add(rating2);

        when(repository.findAllByUser(any())).thenReturn(ratingsByUser);
        service.findAllByUser(new User());

        verify(repository).findAllByUser(any());
    }

    @Test
    void findAllByRaceId() {
        RaceRating rating1 = new RaceRating();
        RaceRating rating2 = new RaceRating();
        Set<RaceRating> ratingsByRace = new HashSet<>();
        ratingsByRace.add(rating1);
        ratingsByRace.add(rating2);

        when(repository.findAllByRaceId(anyLong())).thenReturn(ratingsByRace);
        service.findAllByRaceId(1L);

        verify(repository).findAllByRaceId(anyLong());
    }
}