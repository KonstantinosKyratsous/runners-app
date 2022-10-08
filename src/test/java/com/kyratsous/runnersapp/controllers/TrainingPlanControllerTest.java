package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.TrainingPlanRating;
import com.kyratsous.runnersapp.services.TrainingPlanService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.TrainingPlanFavoriteService;
import com.kyratsous.runnersapp.services.ratings.TrainingPlanRatingService;
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

import static org.hamcrest.Matchers.emptyArray;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainingPlanControllerTest {

    @Mock
    TrainingPlanService trainingPlanService;
    @Mock
    UserService userService;
    @Mock
    TrainingPlanRatingService trainingPlanRatingService;
    @Mock
    TrainingPlanFavoriteService trainingPlanFavoriteService;

    @InjectMocks
    TrainingPlanController controller;

    MockMvc mockMvc;

    Set<TrainingPlan> trainingPlans;
    TrainingPlan plan1 = new TrainingPlan();
    TrainingPlan plan2 = new TrainingPlan();

    Set<TrainingPlanRating> ratings;
    TrainingPlanRating rating1;
    TrainingPlanRating rating2;

    Set<Long> favorites;

    @BeforeEach
    void setUp() {
        trainingPlans = new HashSet<>();
        trainingPlans.add(plan1);
        trainingPlans.add(plan2);

        ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        favorites = new HashSet<>();
        favorites.add(1L);
        favorites.add(2L);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showTrainingPlans() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(trainingPlanService.findAll()).thenReturn(trainingPlans);
        when(trainingPlanFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);

        mockMvc.perform(get("/training-plans"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingPlans/index"))
                .andExpect(model().attribute("trainingPlans", trainingPlans))
                .andExpect(model().attribute("favorites", favorites));
    }

    @Test
    void showTrainingPlan() throws Exception {
        when(trainingPlanService.findById(anyLong())).thenReturn(plan1);
        when(trainingPlanRatingService.findAllByTrainingPlanId(anyLong())).thenReturn(ratings);
        when(trainingPlanFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);

        mockMvc.perform(get("/training-plans/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingPlans/show"))
                .andExpect(model().attribute("trainingPlan", plan1))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("isFavorite", true));
    }

    @Test
    void showMyTrainingPlans() throws Exception {
        when(trainingPlanService.findAllByUser(any())).thenReturn(trainingPlans);

        mockMvc.perform(get("/my-training-plans"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingPlans/my-training-plans"))
                .andExpect(model().attribute("trainingPlans", trainingPlans));
    }

    @Test
    void createTrainingPlanForm() throws Exception {
        mockMvc.perform(get("/my-training-plans/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingPlans/new"));
    }

    @Test
    void createTrainingPlan() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(post("/my-training-plans/new"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-training-plans"));
    }

    @Test
    void updateTrainingPlanForm() throws Exception {
        when(trainingPlanService.findById(anyLong())).thenReturn(plan1);

        mockMvc.perform(get("/my-training-plans/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingPlans/update"))
                .andExpect(model().attribute("trainingPlan", plan1));
    }

    @Test
    void updateTrainingPlan() throws Exception {

        mockMvc.perform(post("/my-training-plans/update"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-training-plans"));
    }

    @Test
    void deleteTrainingPlan() throws Exception {
        mockMvc.perform(get("/my-training-plans/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-training-plans"));
    }

    @Test
    void addTrainingPlanRating() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(trainingPlanService.findById(anyLong())).thenReturn(new TrainingPlan());
        when(trainingPlanRatingService.save(any())).thenReturn(new TrainingPlanRating());

        mockMvc.perform(post("/training-plans/1/rating"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/training-plans/1"));
    }

    @Test
    void deleteTrainingPlanRating() throws Exception {
        mockMvc.perform(get("/training-plans/1/rating/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/training-plans/1"));
    }

    @Test
    void showFavoriteTrainingPlans() throws Exception {
        when(trainingPlanService.findAll()).thenReturn(trainingPlans);
        when(trainingPlanFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/training-plans/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingPlans/favorites"))
                .andExpect(model().attribute("trainingPlans", emptyArray()));
    }

    @Test
    void addTrainingPlanToFavorites() throws Exception {
        mockMvc.perform(get("/training-plans/1/add-favorite"))
                .andExpect(status().isOk());
    }

    @Test
    void removeTrainingPlanFromFavorites() throws Exception {
        mockMvc.perform(get("/training-plans/1/remove-favorite"))
                .andExpect(status().isOk());
    }
}