package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
import com.kyratsous.runnersapp.repositories.favorites.RaceFavoriteRepository;
import com.kyratsous.runnersapp.services.RaceService;
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
class RaceFavoriteServiceTest {

    @InjectMocks
    RaceFavoriteService service;

    @Mock
    RaceFavoriteRepository repository;

    @Mock
    RaceService raceService;

    @Test
    void findAll() {
        RaceFavorite favorite1 = new RaceFavorite();
        RaceFavorite favorite2 = new RaceFavorite();
        Set<RaceFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(repository.findAll()).thenReturn(favorites);

        Set<RaceFavorite> favoritesAll = service.findAll();

        assertNotNull(favoritesAll);
        assertEquals(2, favoritesAll.size());

        verify(repository).findAll();
    }

    @Test
    void save() {
        User user = new User();
        Race race = new Race();
        RaceFavorite favorite = new RaceFavorite(race, user);

        when(repository.existsRaceFavoriteByRaceAndUser(any(), any())).thenReturn(false);

        service.save(favorite);

        verify(repository).save(any());
    }

    @Test
    void deleteByObjectId() {
        when(raceService.findById(anyLong())).thenReturn(new Race());
        service.deleteByObjectId(1L, new User());

        verify(repository).deleteRaceFavoriteByRaceAndUser(any(), any());
    }

    @Test
    void findAllObjectIdsByUser() {
        RaceFavorite favorite1 = new RaceFavorite();
        RaceFavorite favorite2 = new RaceFavorite();
        Set<RaceFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(repository.findAll()).thenReturn(favorites);

        Set<Long> ids = service.findAllObjectIdsByUser(new User());

        assertNotNull(ids);
        assertEquals(0, ids.size());

        verify(repository).findAll();
    }
}