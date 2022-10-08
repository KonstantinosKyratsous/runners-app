package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.DietFavorite;
import com.kyratsous.runnersapp.repositories.favorites.DietFavoriteRepository;
import com.kyratsous.runnersapp.services.DietService;
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
class DietFavoriteServiceTest {

    @InjectMocks
    DietFavoriteService dietFavoriteService;

    @Mock
    DietFavoriteRepository dietFavoriteRepository;

    @Mock
    DietService dietService;

    @Test
    void findAll() {
        DietFavorite favorite1 = new DietFavorite();
        DietFavorite favorite2 = new DietFavorite();
        Set<DietFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(dietFavoriteRepository.findAll()).thenReturn(favorites);

        Set<DietFavorite> favoritesAll = dietFavoriteService.findAll();

        assertNotNull(favoritesAll);
        assertEquals(2, favorites.size());

        verify(dietFavoriteRepository).findAll();
    }

    @Test
    void save() {
        User user = new User();
        Diet diet = new Diet();
        DietFavorite favorite = new DietFavorite(diet, user);

        when(dietFavoriteRepository.existsDietFavoriteByDietAndUser(any(), any())).thenReturn(false);

        dietFavoriteService.save(favorite);

        verify(dietFavoriteRepository).save(any());
    }

    @Test
    void deleteByObjectId() {
        when(dietService.findById(anyLong())).thenReturn(new Diet());
        dietFavoriteService.deleteByObjectId(1L, new User());

        verify(dietFavoriteRepository).deleteDietFavoriteByDietAndUser(any(), any());
    }

    @Test
    void findAllObjectIdsByUser() {
        DietFavorite favorite1 = new DietFavorite();
        DietFavorite favorite2 = new DietFavorite();
        Set<DietFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(dietFavoriteRepository.findAll()).thenReturn(favorites);

        Set<Long> ids = dietFavoriteService.findAllObjectIdsByUser(new User());

        assertNotNull(ids);
        assertEquals(0, ids.size());
        verify(dietFavoriteRepository).findAll();
    }
}