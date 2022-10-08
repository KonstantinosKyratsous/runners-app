package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.*;
import com.kyratsous.runnersapp.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    UserService userService;

    @Mock
    RaceService raceService;

    @Mock
    ProductService productService;

    @Mock
    TrainingPlanService trainingPlanService;

    @Mock
    DietService dietService;

    @InjectMocks
    HomeController homeController;

    MockMvc mockMvc;

    Set<Race> races;
    Set<TrainingPlan> trainingPlans;
    Set<Product> products;
    Set<Diet> diets;

    User user;

    @BeforeEach
    void setUp() {
        races = new HashSet<>();
        races.add(new Race());
        races.add(new Race());

        trainingPlans = new HashSet<>();
        trainingPlans.add(new TrainingPlan());
        trainingPlans.add(new TrainingPlan());

        products = new HashSet<>();
        products.add(new Product());
        products.add(new Product());

        diets = new HashSet<>();
        diets.add(new Diet());
        diets.add(new Diet());

        user = new User();
        Set<String> authorities = new HashSet<>();
        authorities.add("ORGANIZER");
        authorities.add("COACH");
        authorities.add("NUTRITIONIST");
        user.setAuthorities(authorities);

        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void getHomePageForAthlete() throws Exception {
        when(raceService.findFilteredRaces(any())).thenReturn(races);
        when(userService.getCurrentUser()).thenReturn(user);

        Cookie cookie = new Cookie("VIEW-AS", "ATHLETE");

        mockMvc.perform(get("/").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(view().name("home-pages/home"))
                .andExpect(model().attribute("races", hasSize(2)));

    }

    @Test
    void getHomePageForOrganizer() throws Exception {
        when(raceService.findAllByUser(any())).thenReturn(races);
        when(userService.getCurrentUser()).thenReturn(user);

        Cookie cookie = new Cookie("VIEW-AS", "ORGANIZER");

        mockMvc.perform(get("/").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(view().name("home-pages/home-organizer"))
                .andExpect(model().attribute("races", hasSize(2)));

    }

    @Test
    void getHomePageForCoach() throws Exception {
        when(trainingPlanService.findAllByUser(any())).thenReturn(trainingPlans);
        when(productService.findAllByUser(any())).thenReturn(products);
        when(userService.getCurrentUser()).thenReturn(user);

        Cookie cookie = new Cookie("VIEW-AS", "COACH");

        mockMvc.perform(get("/").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(view().name("home-pages/home-coach"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attribute("trainingPlans", hasSize(2)));

    }

    @Test
    void getHomePageForNutritionist() throws Exception {
        when(dietService.findAllByUser(any())).thenReturn(diets);
        when(userService.getCurrentUser()).thenReturn(user);

        Cookie cookie = new Cookie("VIEW-AS", "NUTRITIONIST");

        mockMvc.perform(get("/").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(view().name("home-pages/home-nutritionist"))
                .andExpect(model().attribute("diets", hasSize(2)));

    }
}