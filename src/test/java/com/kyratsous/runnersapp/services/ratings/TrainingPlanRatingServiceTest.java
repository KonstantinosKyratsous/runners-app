package com.kyratsous.runnersapp.services.ratings;


import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.TrainingPlanRating;
import com.kyratsous.runnersapp.repositories.ratings.TrainingPlanRatingRepository;
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
class TrainingPlanRatingServiceTest {

    @InjectMocks
    TrainingPlanRatingService service;

    @Mock
    TrainingPlanRatingRepository repository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        TrainingPlanRating rating1 = new TrainingPlanRating();
        TrainingPlanRating rating2 = new TrainingPlanRating();
        Set<TrainingPlanRating> ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        when(repository.findAll()).thenReturn(ratings);

        Set<TrainingPlanRating> ratingsAll = service.findAll();

        assertNotNull(ratingsAll);
        assertEquals(2, ratingsAll.size());

        verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new TrainingPlanRating()));
        service.findById(1L);

        verify(repository).findById(anyLong());
    }

    @Test
    void save() {
        when(repository.save(any())).thenReturn(new TrainingPlanRating());
        service.save(new TrainingPlanRating());

        verify(repository).save(any());
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setUsername("test");
        TrainingPlanRating rating = new TrainingPlanRating();
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
        TrainingPlanRating rating1 = new TrainingPlanRating();
        TrainingPlanRating rating2 = new TrainingPlanRating();
        Set<TrainingPlanRating> ratingsByUser = new HashSet<>();
        ratingsByUser.add(rating1);
        ratingsByUser.add(rating2);

        when(repository.findAllByUser(any())).thenReturn(ratingsByUser);
        service.findAllByUser(new User());

        verify(repository).findAllByUser(any());
    }

    @Test
    void findAllByTrainingPlanId() {
        TrainingPlanRating rating1 = new TrainingPlanRating();
        TrainingPlanRating rating2 = new TrainingPlanRating();
        Set<TrainingPlanRating> ratingsByTrainingPlan = new HashSet<>();
        ratingsByTrainingPlan.add(rating1);
        ratingsByTrainingPlan.add(rating2);

        when(repository.findAllByTrainingPlanId(anyLong())).thenReturn(ratingsByTrainingPlan);
        service.findAllByTrainingPlanId(1L);

        verify(repository).findAllByTrainingPlanId(anyLong());
    }
}