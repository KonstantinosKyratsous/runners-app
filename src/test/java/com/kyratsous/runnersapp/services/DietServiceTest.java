package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.DietRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {

    @InjectMocks
    DietService dietService;

    @Mock
    DietRepository dietRepository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        Diet diet1 = new Diet();
        Diet diet2 = new Diet();
        Set<Diet> diets = new HashSet<>();
        diets.add(diet1);
        diets.add(diet2);

        when(dietRepository.findAll()).thenReturn(diets);

        Set<Diet> dietsAll = dietService.findAll();

        assertNotNull(dietsAll);
        assertEquals(2, dietsAll.size());

        verify(dietRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(dietRepository.findById(anyLong())).thenReturn(Optional.of(new Diet()));

        Diet diet = dietService.findById(1L);

        assertNotNull(diet);
        verify(dietRepository).findById(anyLong());
    }

    @Test
    void save() {
        when(dietRepository.save(any())).thenReturn(new Diet());

        Diet diet = dietService.save(new Diet());

        assertNotNull(diet);
        verify(dietRepository).save(any());
    }

    @Test
    void deleteById() {
        Diet diet = new Diet();
        User user = new User();
        user.setUsername("test");
        diet.setNutritionist(user);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.setContext(context);

        when(dietRepository.findById(anyLong())).thenReturn(Optional.of(diet));
        dietService.deleteById(1L);

        verify(dietRepository).deleteById(anyLong());
    }

    @Test
    void findAllByUser() {
        Diet diet1 = new Diet();
        Diet diet2 = new Diet();
        Set<Diet> dietsByUser = new HashSet<>();
        dietsByUser.add(diet1);
        dietsByUser.add(diet2);

        when(dietRepository.findAllByNutritionist(notNull())).thenReturn(dietsByUser);

        Set<Diet> diets = dietService.findAllByUser(new User());

        assertEquals(2, diets.size());
        verify(dietRepository).findAllByNutritionist(any());
    }
}