package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.TrainingPlanFavorite;
import com.kyratsous.runnersapp.repositories.favorites.TrainingPlanFavoriteRepository;
import com.kyratsous.runnersapp.services.TrainingPlanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingPlanFavoriteServiceTest {

    @InjectMocks
    TrainingPlanFavoriteService service;

    @Mock
    TrainingPlanFavoriteRepository repository;

    @Mock
    TrainingPlanService trainingPlanService;

    @Test
    void findAll() {
        TrainingPlanFavorite favorite1 = new TrainingPlanFavorite();
        TrainingPlanFavorite favorite2 = new TrainingPlanFavorite();
        Set<TrainingPlanFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(repository.findAll()).thenReturn(favorites);

        Set<TrainingPlanFavorite> favoritesAll = service.findAll();

        assertNotNull(favoritesAll);
        assertEquals(2, favoritesAll.size());

        verify(repository).findAll();
    }

    @Test
    void save() {
        User user = new User();
        TrainingPlan trainingPlan = new TrainingPlan();
        TrainingPlanFavorite favorite = new TrainingPlanFavorite(trainingPlan, user);

        when(repository.existsTrainingPlanFavoriteByTrainingPlanAndUser(any(), any())).thenReturn(false);

        service.save(favorite);

        verify(repository).save(any());
    }

    @Test
    void deleteByObjectId() {
        when(trainingPlanService.findById(anyLong())).thenReturn(new TrainingPlan());
        service.deleteByObjectId(1L, new User());

        verify(repository).deleteTrainingPlanFavoriteByTrainingPlanAndUser(any(), any());
    }

    @Test
    void findAllObjectIdsByUser() {
        TrainingPlanFavorite favorite1 = new TrainingPlanFavorite();
        TrainingPlanFavorite favorite2 = new TrainingPlanFavorite();
        Set<TrainingPlanFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(repository.findAll()).thenReturn(favorites);

        Set<Long> ids = service.findAllObjectIdsByUser(new User());

        assertNotNull(ids);
        assertEquals(0, ids.size());

        verify(repository).findAll();
    }
}