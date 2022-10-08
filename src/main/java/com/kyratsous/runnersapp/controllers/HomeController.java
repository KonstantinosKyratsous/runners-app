package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    private final RaceService raceService;
    private final UserService userService;
    private final ProductService productService;
    private final DietService dietService;
    private final TrainingPlanService trainingPlanService;

    public HomeController(RaceService raceService, UserService userService, ProductService productService, DietService dietService, TrainingPlanService trainingPlanService) {
        this.raceService = raceService;
        this.userService = userService;
        this.productService = productService;
        this.dietService = dietService;
        this.trainingPlanService = trainingPlanService;
    }

    @RequestMapping({"", "/", "/home", "/home.html"})
    public String getHomePage(@CookieValue(value = "VIEW-AS", defaultValue = "GUEST") String type, Model model) {
        User currentUser = userService.getCurrentUser();

        switch (type) {
            case "ORGANIZER": if (currentUser != null && currentUser.getAuthorities().contains("ORGANIZER")) {
                                model.addAttribute("races", raceService.findAllByUser(currentUser));
                                return "home-pages/home-organizer";}

            case "COACH": if (currentUser != null && currentUser.getAuthorities().contains("COACH")) {
                            model.addAttribute("products", productService.findAllByUser(currentUser));
                            model.addAttribute("trainingPlans", trainingPlanService.findAllByUser(currentUser));
                            return "home-pages/home-coach";}

            case "NUTRITIONIST": if (currentUser != null && currentUser.getAuthorities().contains("NUTRITIONIST")) {
                                    model.addAttribute("diets", dietService.findAllByUser(currentUser));
                                    return "home-pages/home-nutritionist";}

            default: Map<String, String> filters = new HashMap<>();
                     filters.put("sort", "most-recent");
                     model.addAttribute("races", raceService.findFilteredRaces(filters));
                     return "home-pages/home"; // GUEST, ATHLETE, EMPTY
        }
    }
}
