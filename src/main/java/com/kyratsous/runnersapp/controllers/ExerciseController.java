package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ExerciseFavorite;
import com.kyratsous.runnersapp.model.ratings.ExerciseRating;
import com.kyratsous.runnersapp.services.ExerciseService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.ExerciseFavoriteService;
import com.kyratsous.runnersapp.services.ratings.ExerciseRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Controller
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;
    private final ExerciseRatingService exerciseRatingService;
    private final ExerciseFavoriteService exerciseFavoriteService;

    public ExerciseController(ExerciseService exerciseService, UserService userService, ExerciseRatingService exerciseRatingService, ExerciseFavoriteService exerciseFavoriteService) {
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.exerciseRatingService = exerciseRatingService;
        this.exerciseFavoriteService = exerciseFavoriteService;
    }

    @RequestMapping("/exercises")
    public String showExercises(Model model, @RequestParam(required=false) Map<String,String> filters,
                                @RequestParam(value = "pref", defaultValue = "false") boolean pref) {
        User currentUser = userService.getCurrentUser();

        if (currentUser != null && pref) {
            return "redirect:/exercises?" + currentUser.getPreferencesToString(true, true, true);
        }

        model.addAttribute("exercises", filters.isEmpty()? exerciseService.findAll(): exerciseService.findAllByFilters(filters));
        model.addAttribute("favorites", exerciseFavoriteService.findAllObjectIdsByUser(currentUser));

        return "exercises/index";
    }

    @RequestMapping("/exercises/{id}")
    public String showExercise(@PathVariable Long id, Model model) {
        model.addAttribute("exercise", exerciseService.findById(id));
        model.addAttribute("ratings", exerciseRatingService.findAllByExerciseId(id));
        model.addAttribute("newRating", new ExerciseRating());
        model.addAttribute("isFavorite", exerciseFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(id));

        return "exercises/show";
    }

    @RequestMapping("/my-exercises")
    public String showMyExercises(Model model) {
        model.addAttribute("exercises", exerciseService.findAllByUser(userService.getCurrentUser()));

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
        exercise.setDate(new Date());

        exerciseService.save(exercise);
        return "redirect:/my-exercises";
    }

    @RequestMapping("/my-exercises/{id}/update")
    public String updateExerciseForm(@PathVariable Long id, Model model) {
        Exercise exercise = exerciseService.findById(id);
        model.addAttribute("exercise", exercise);

        return "exercises/update";
    }

    @PostMapping("/my-exercises/update")
    public String updateExercise(@ModelAttribute("exercise") Exercise exercise) {
        exerciseService.update(exercise);
        return "redirect:/my-exercises";
    }

    @GetMapping("/my-exercises/{id}/delete")
    public String deleteExercise(@PathVariable Long id) {
        exerciseService.deleteById(id);
        return "redirect:/my-exercises";
    }

    @PostMapping("/exercises/{exercise_id}/rating")
    public String addExerciseRating(@PathVariable Long exercise_id, @ModelAttribute("newRating") ExerciseRating exerciseRating) {
        exerciseRating.setUser(userService.getCurrentUser());
        exerciseRating.setExercise(exerciseService.findById(exercise_id));
        exerciseRatingService.save(exerciseRating);

        return "redirect:/exercises/" + exercise_id;
    }

    @GetMapping("/exercises/{id}/rating/{rate_id}/delete")
    public String deleteExerciseRating(@PathVariable Long id, @PathVariable Long rate_id) {
        exerciseRatingService.deleteById(rate_id);

        return "redirect:/exercises/" + id;
    }

    @RequestMapping("/exercises/favorites")
    public String showFavoriteExercises(Model model) {
        model.addAttribute("exercises",
                exerciseService.findAll().stream().filter(exercise ->
                        exerciseFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(exercise.getId())).toArray());

        return "exercises/favorites";
    }

    @GetMapping("/exercises/{id}/add-favorite")
    @ResponseBody
    public String addExerciseToFavorites(@PathVariable Long id) {
        exerciseFavoriteService.save(new ExerciseFavorite(exerciseService.findById(id), userService.getCurrentUser()));
        return "ok";
    }

    @GetMapping("/exercises/{id}/remove-favorite")
    @ResponseBody
    public String removeExerciseFromFavorites(@PathVariable Long id) {
        exerciseFavoriteService.deleteByObjectId(id, userService.getCurrentUser());
        return "ok";
    }
}