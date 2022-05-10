package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.services.ExerciseService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;

    public ExerciseController(ExerciseService exerciseService, UserService userService) {
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @RequestMapping("/exercises")
    public String showExercises(Model model) {
        model.addAttribute("exercises", exerciseService.findAll());

        return "exercises/index";
    }

    @RequestMapping("/my-exercises")
    public String showMyExercises(Model model) {
        model.addAttribute("exercises", exerciseService.findAllByUserId(userService.getCurrentUser()));

        return "exercises/my-exercises";
    }

    @RequestMapping("/my-exercises/new")
    public String createExerciseForm(Model model) {
        Exercise exercise = new Exercise();
        model.addAttribute("exercise", exercise);

        return "exercises/new";
    }

    @PostMapping("/my-exercises/new")
    public String createExercise(@ModelAttribute("exercise") Exercise exercise) {
        exercise.setCoach(userService.getCurrentUser());

        exerciseService.save(exercise);
        return "redirect:/my-exercises";
    }

    @RequestMapping("/my-exercises/{id}/update")
    public String updateExerciseForm(@PathVariable Long id, Model model) {
        Exercise exercise = exerciseService.findById(id);
        model.addAttribute("exercise", exercise);

        return "exercises/update";
    }

    @PostMapping("/my-exercises/{id}/update")
    public String updateExercise(@PathVariable Long id, @ModelAttribute("exercise") Exercise exercise) {
        exerciseService.update(exercise);
        return "redirect:/my-exercises";
    }

    @GetMapping("/my-exercises/{id}/delete")
    public String deleteExercise(@PathVariable Long id) {
        exerciseService.deleteById(id);
        return "redirect:/my-exercises";
    }
}
