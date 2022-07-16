package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.model.ExerciseRating;
import com.kyratsous.runnersapp.services.ExerciseRatingService;
import com.kyratsous.runnersapp.services.ExerciseService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;
    private final ExerciseRatingService exerciseRatingService;

    public ExerciseController(ExerciseService exerciseService, UserService userService, ExerciseRatingService exerciseRatingService) {
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.exerciseRatingService = exerciseRatingService;
    }

    @RequestMapping("/exercises")
    public String showExercises(Model model) {
        model.addAttribute("exercises", exerciseService.findAll());

        return "exercises/index";
    }

    @RequestMapping("/exercises/{id}")
    public String showExercise(@PathVariable Long id, Model model) {
        model.addAttribute("exercise", exerciseService.findById(id));
        model.addAttribute("ratings", exerciseRatingService.findAllByExerciseId(id));
        model.addAttribute("newRating", new ExerciseRating());

        return "exercises/show";
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

    @PostMapping("/exercises/{id}/rating")
    public String addExerciseRating(@PathVariable Long id, @ModelAttribute("newRating") ExerciseRating exerciseRating) {
        exerciseRating.setUser(userService.getCurrentUser());
        exerciseRating.setExercise(exerciseService.findById(id));
        exerciseRatingService.save(exerciseRating);

        return "redirect:/exercises/" + id;
    }

    @GetMapping("/exercises/{id}/rating/{rate_id}/delete")
    public String deleteExerciseRating(@PathVariable Long id, @PathVariable Long rate_id) {
        exerciseRatingService.deleteById(rate_id);

        return "redirect:/exercises/" + id;
    }
}
