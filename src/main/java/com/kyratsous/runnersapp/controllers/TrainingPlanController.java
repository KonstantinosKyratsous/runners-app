package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.TrainingPlanFavorite;
import com.kyratsous.runnersapp.model.ratings.TrainingPlanRating;
import com.kyratsous.runnersapp.services.TrainingPlanService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.TrainingPlanFavoriteService;
import com.kyratsous.runnersapp.services.ratings.TrainingPlanRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Controller
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;
    private final UserService userService;
    private final TrainingPlanRatingService trainingPlanRatingService;
    private final TrainingPlanFavoriteService trainingPlanFavoriteService;

    public TrainingPlanController(TrainingPlanService trainingPlanService, UserService userService, TrainingPlanRatingService trainingPlanRatingService, TrainingPlanFavoriteService trainingPlanFavoriteService) {
        this.trainingPlanService = trainingPlanService;
        this.userService = userService;
        this.trainingPlanRatingService = trainingPlanRatingService;
        this.trainingPlanFavoriteService = trainingPlanFavoriteService;
    }

    @RequestMapping("/training-plans")
    public String showTrainingPlans(Model model, @RequestParam(required=false) Map<String,String> filters,
                                @RequestParam(value = "pref", defaultValue = "false") boolean pref) {
        User currentUser = userService.getCurrentUser();

        if (currentUser != null && pref) {
            return "redirect:/training-plans?" + currentUser.getPreferencesToString(true, true, true);
        }

        model.addAttribute("trainingPlans", filters.isEmpty()? trainingPlanService.findAll(): trainingPlanService.findAllByFilters(filters));
        model.addAttribute("favorites", trainingPlanFavoriteService.findAllObjectIdsByUser(currentUser));

        return "trainingPlans/index";
    }

    @RequestMapping("/training-plans/{id}")
    public String showTrainingPlan(@PathVariable Long id, Model model) {
        model.addAttribute("trainingPlan", trainingPlanService.findById(id));
        model.addAttribute("ratings", trainingPlanRatingService.findAllByTrainingPlanId(id));
        model.addAttribute("newRating", new TrainingPlanRating());
        model.addAttribute("isFavorite", trainingPlanFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(id));

        return "trainingPlans/show";
    }

    @RequestMapping("/my-training-plans")
    public String showMyTrainingPlans(Model model) {
        model.addAttribute("trainingPlans", trainingPlanService.findAllByUser(userService.getCurrentUser()));

        return "trainingPlans/my-training-plans";
    }

    @RequestMapping("/my-training-plans/new")
    public String createTrainingPlanForm(Model model) {
        TrainingPlan trainingPlan = new TrainingPlan();
        model.addAttribute("trainingPlan", trainingPlan);

        return "trainingPlans/new";
    }

    @PostMapping("/my-training-plans/new")
    public String createTrainingPlan(@ModelAttribute("trainingPlan") TrainingPlan trainingPlan) {
        trainingPlan.setCoach(userService.getCurrentUser());
        trainingPlan.setDate(new Date());

        trainingPlanService.save(trainingPlan);
        return "redirect:/my-training-plans";
    }

    @RequestMapping("/my-training-plans/{id}/update")
    public String updateTrainingPlanForm(@PathVariable Long id, Model model) {
        TrainingPlan trainingPlan = trainingPlanService.findById(id);
        model.addAttribute("trainingPlan", trainingPlan);

        return "trainingPlans/update";
    }

    @PostMapping("/my-training-plans/update")
    public String updateTrainingPlan(@ModelAttribute("trainingPlan") TrainingPlan trainingPlan) {
        trainingPlanService.update(trainingPlan);
        return "redirect:/my-training-plans";
    }

    @GetMapping("/my-training-plans/{id}/delete")
    public String deleteTrainingPlan(@PathVariable Long id) {
        trainingPlanService.deleteById(id);
        return "redirect:/my-training-plans";
    }

    @PostMapping("/training-plans/{training_plan_id}/rating")
    public String addTrainingPlanRating(@PathVariable Long training_plan_id, @ModelAttribute("newRating") TrainingPlanRating trainingPlanRating) {
        trainingPlanRating.setUser(userService.getCurrentUser());
        trainingPlanRating.setTrainingPlan(trainingPlanService.findById(training_plan_id));
        trainingPlanRatingService.save(trainingPlanRating);

        return "redirect:/training-plans/" + training_plan_id;
    }

    @GetMapping("/training-plans/{id}/rating/{rate_id}/delete")
    public String deleteTrainingPlanRating(@PathVariable Long id, @PathVariable Long rate_id) {
        trainingPlanRatingService.deleteById(rate_id);

        return "redirect:/training-plans/" + id;
    }

    @RequestMapping("/training-plans/favorites")
    public String showFavoriteTrainingPlans(Model model) {
        model.addAttribute("trainingPlans",
                trainingPlanService.findAll().stream().filter(trainingPlan ->
                        trainingPlanFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(trainingPlan.getId())).toArray());

        return "trainingPlans/favorites";
    }

    @GetMapping("/training-plans/{id}/add-favorite")
    @ResponseBody
    public String addTrainingPlanToFavorites(@PathVariable Long id) {
        trainingPlanFavoriteService.save(new TrainingPlanFavorite(trainingPlanService.findById(id), userService.getCurrentUser()));
        return "ok";
    }

    @GetMapping("/training-plans/{id}/remove-favorite")
    @ResponseBody
    public String removeTrainingPlanFromFavorites(@PathVariable Long id) {
        trainingPlanFavoriteService.deleteByObjectId(id, userService.getCurrentUser());
        return "ok";
    }
}