package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.RaceRating;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import com.kyratsous.runnersapp.services.ratings.RaceRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.emptyArray;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RaceControllerTest {

    @Mock
    RaceService raceService;
    @Mock
    UserService userService;
    @Mock
    RaceRatingService raceRatingService;
    @Mock
    RaceFavoriteService raceFavoriteService;

    @InjectMocks
    RaceController controller;

    MockMvc mockMvc;

    Set<Race> races;
    Race race1 = new Race();
    Race race2 = new Race();

    Set<RaceRating> ratings;
    RaceRating rating1;
    RaceRating rating2;

    Set<Long> favorites;

    @BeforeEach
    void setUp() {
        races = new HashSet<>();
        races.add(race1);
        races.add(race2);

        ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        favorites = new HashSet<>();
        favorites.add(1L);
        favorites.add(2L);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllRaces() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(raceService.findAll()).thenReturn(races);
        when(raceFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);

        mockMvc.perform(get("/races"))
                .andExpect(status().isOk())
                .andExpect(view().name("races/index"))
                .andExpect(model().attribute("races", races))
                .andExpect(model().attribute("favorites", favorites));
    }

    @Test
    void getMyRaces() throws Exception {
        when(raceService.findAllByUser(any())).thenReturn(races);

        mockMvc.perform(get("/my-races"))
                .andExpect(status().isOk())
                .andExpect(view().name("races/my-races"))
                .andExpect(model().attribute("races", races));
    }

    @Test
    void createRaceForm() throws Exception {
        mockMvc.perform(get("/my-races/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("races/new"));
    }

    @Test
    void createRace() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());

        MockMultipartFile file = new MockMultipartFile("file", "file", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/my-races/new")
                        .file("file-pdf" ,file.getBytes())
                        .param("dateString", "2022-10-13"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-races"));
    }

    @Test
    void updateRaceForm() throws Exception {
        when(raceService.findById(anyLong())).thenReturn(race1);

        mockMvc.perform(get("/my-races/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("races/update"))
                .andExpect(model().attribute("race", race1));
    }

    @Test
    void updateRace() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file-pdf", "file-pdf", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/my-races/update")
                    .file("file-pdf", file.getBytes())
                    .param("dateString", "2022-10-13"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-races"));
    }

    @Test
    void deleteRace() throws Exception {
        mockMvc.perform(get("/my-races/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-races"));
    }

    @Test
    void showRace() throws Exception {
        when(raceService.findById(anyLong())).thenReturn(race1);
        when(raceRatingService.findAllByRaceId(anyLong())).thenReturn(ratings);
        when(raceFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);

        mockMvc.perform(get("/races/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("races/show"))
                .andExpect(model().attribute("race", race1))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("isFavorite", true));
    }

    @Test
    void showRaceImage() throws Exception {
        race1.setFile(new byte[0]);
        when(raceService.findById(anyLong())).thenReturn(race1);

        mockMvc.perform(get("/races/1/documentation"))
                .andExpect(status().isOk());
    }

    @Test
    void addRaceRating() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(raceService.findById(anyLong())).thenReturn(new Race());
        when(raceRatingService.save(any())).thenReturn(new RaceRating());

        mockMvc.perform(post("/races/1/rating"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/races/1"));
    }

    @Test
    void deleteRaceRating() throws Exception {
        mockMvc.perform(get("/races/1/rating/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/races/1"));
    }

    @Test
    void showFavoriteRaces() throws Exception {
        when(raceService.findAll()).thenReturn(races);
        when(raceFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/races/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("races/favorites"))
                .andExpect(model().attribute("races", emptyArray()));
    }

    @Test
    void addToFavorites() throws Exception {
        mockMvc.perform(get("/races/1/add-favorite"))
                .andExpect(status().isOk());
    }

    @Test
    void removeFromFavorites() throws Exception {
        mockMvc.perform(get("/races/1/remove-favorite"))
                .andExpect(status().isOk());
    }
}