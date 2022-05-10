package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.services.DietService;
import com.kyratsous.runnersapp.services.ExerciseService;
import com.kyratsous.runnersapp.services.RaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final RaceService raceService;
    private final DietService dietService;
    private final ExerciseService exerciseService;

    public HomeController(RaceService raceService, DietService dietService, ExerciseService exerciseService) {
        this.raceService = raceService;
        this.dietService = dietService;
        this.exerciseService = exerciseService;
    }

    @RequestMapping({"", "/", "/home", "/home.html"})
    public String home(Model model) {
        model.addAttribute("races", raceService.findAll());
        model.addAttribute("exercises", exerciseService.findAll());
        model.addAttribute("diets", dietService.findAll());

        return "home";
    }
}
