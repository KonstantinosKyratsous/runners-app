package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.RaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RaceServiceTest {

    @InjectMocks
    RaceService raceService;

    @Mock
    RaceRepository raceRepository;

    @Mock
    SecurityContext context;

    @Mock
    Authentication authentication;

    Race race;

    @BeforeEach
    void setUp() {
        race = new Race(new User(), "Test Title", "Test Description", new HashSet<>(), new HashSet<>(),
                0.0, 0.0, "Location", new Date(), 0.0, "test_link.com", new byte[0]);
        race.setId(1L);
    }

    @Test
    void findAll() {
        Race race = new Race();
        Set<Race> racesData = new HashSet<>();
        racesData.add(race);

        Mockito.when(raceRepository.findAll()).thenReturn(racesData);

        Set<Race> races = raceService.findAll();

        assertNotNull(races);
        assertEquals(races.size(), 1);
        verify(raceRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        Mockito.when(raceRepository.findById(anyLong())).thenReturn(Optional.of(race));

        Race raceT = raceService.findById(1L);

        assertNotNull(raceT);
        assertEquals(race, raceT);
    }

    @Test
    void save() {
        Race raceToSave = new Race(new User(), "Test Title 2", "Test Description 2", new HashSet<>(), new HashSet<>(),
                0.0, 0.0, "Location", new Date(), 0.0, "test_link.com", new byte[0]);

        when(raceRepository.save(any())).thenReturn(race);

        Race savedRace = raceService.save(raceToSave);

        assertNotNull(savedRace);
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setUsername("test");
        Race race = new Race();
        race.setOrganizer(user);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.setContext(context);

        when(raceRepository.findById(anyLong())).thenReturn(Optional.of(race));
        raceService.deleteById(1L);

        verify(raceRepository).deleteById(anyLong());
    }

    @Test
    void findAllByUser() {
        Race race1 = new Race();
        Race race2 = new Race();
        Set<Race> racesByUser = new HashSet<>();
        racesByUser.add(race1);
        racesByUser.add(race2);

        when(raceRepository.findAllByOrganizer(notNull())).thenReturn(racesByUser);

        Set<Race> races = raceService.findAllByUser(new User());

        assertEquals(2, races.size());
        verify(raceRepository).findAllByOrganizer(any());
    }

}
