package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.DietRating;
import com.kyratsous.runnersapp.services.DietService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.DietFavoriteService;
import com.kyratsous.runnersapp.services.ratings.DietRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DietControllerTest {

    @Mock
    UserService userService;

    @Mock
    DietService dietService;

    @Mock
    DietRatingService dietRatingService;

    @Mock
    DietFavoriteService dietFavoriteService;

    @InjectMocks
    DietController dietController;

    MockMvc mockMvc;

    Set<Diet> diets;
    Diet diet1 = new Diet();
    Diet diet2 = new Diet();
    Set<DietRating> dietRatings;
    DietRating rating1 = new DietRating();
    DietRating rating2 = new DietRating();
    Set<Long> dietFavorites;

    @BeforeEach
    void setUp() {
        diets = new HashSet<>();
        diets.add(diet1);
        diets.add(diet2);

        dietRatings = new HashSet<>();
        dietRatings.add(rating1);
        dietRatings.add(rating2);

        dietFavorites = new HashSet<>();
        dietFavorites.add(1L);
        dietFavorites.add(2L);

        mockMvc = MockMvcBuilders.standaloneSetup(dietController).build();
    }

    @Test
    void showDiets() throws Exception {
        when(dietService.findAll()).thenReturn(diets);
        when(dietFavoriteService.findAllObjectIdsByUser(any())).thenReturn(dietFavorites);

        mockMvc.perform(get("/diets"))
                .andExpect(status().isOk())
                .andExpect(view().name("diets/index"))
                .andExpect(model().attribute("diets", hasSize(2)))
                .andExpect(model().attribute("favorites", hasSize(2)));
    }

    @Test
    void showDiet() throws Exception {
        when(dietService.findById(anyLong())).thenReturn(diet1);
        when(dietRatingService.findAllByDietId(anyLong())).thenReturn(dietRatings);
        when(dietFavoriteService.findAllObjectIdsByUser(any())).thenReturn(new HashSet<>());

        mockMvc.perform(get("/diets/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("diets/show"))
                .andExpect(model().attribute("diet", equalToObject(diet1)))
                .andExpect(model().attribute("ratings", hasSize(2)))
                .andExpect(model().attribute("isFavorite", false));
    }

    @Test
    void showMyDiets() throws Exception {
        when(dietService.findAllByUser(any())).thenReturn(diets);

        mockMvc.perform(get("/my-diets"))
                .andExpect(status().isOk())
                .andExpect(view().name("diets/my-diets"))
                .andExpect(model().attribute("diets", hasSize(2)));
    }

    @Test
    void createDietForm() throws Exception {
        mockMvc.perform(get("/my-diets/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("diets/new"));
    }

    @Test
    void createDiet() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(dietService.save(any())).thenReturn(new Diet());

        mockMvc.perform(post("/my-diets/new").requestAttr("diet", new Diet()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-diets"));
    }

    @Test
    void updateDietForm() throws Exception {
        User user = new User();
        user.setId(1L);
        Diet diet = new Diet();
        diet.setId(1L);
        diet.setNutritionist(user);

        when(dietService.findById(anyLong())).thenReturn(diet);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/my-diets/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("diets/update"))
                .andExpect(model().attribute("diet", diet));
    }

    @Test
    void updateDiet() throws Exception {
        mockMvc.perform(post("/my-diets/update").requestAttr("diet", new Diet()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-diets"));
    }

    @Test
    void deleteDiet() throws Exception {
        mockMvc.perform(get("/my-diets/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-diets"));
    }

    @Test
    void addDietRating() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(dietService.findById(anyLong())).thenReturn(new Diet());
        when(dietRatingService.save(any())).thenReturn(new DietRating());

        mockMvc.perform(post("/diets/1/rating"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/diets/1"));

    }

    @Test
    void deleteDietRating() throws Exception {
        mockMvc.perform(get("/diets/1/rating/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/diets/1"));
    }

    @Test
    void showFavoriteDiets() throws Exception {
        when(dietService.findAll()).thenReturn(diets);
        when(dietFavoriteService.findAllObjectIdsByUser(any())).thenReturn(dietFavorites);
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/diets/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("diets/favorites"))
                .andExpect(model().attribute("diets", emptyArray()));
    }

    @Test
    void addDietToFavorites() throws Exception {
        mockMvc.perform(get("/diets/1/add-favorite"))
                .andExpect(status().isOk());
    }

    @Test
    void removeDietFromFavorites() throws Exception {
        mockMvc.perform(get("/diets/1/remove-favorite"))
                .andExpect(status().isOk());
    }
}