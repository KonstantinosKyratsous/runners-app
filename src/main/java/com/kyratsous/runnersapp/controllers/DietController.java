package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.favorites.DietFavorite;
import com.kyratsous.runnersapp.model.ratings.DietRating;
import com.kyratsous.runnersapp.services.DietService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.DietFavoriteService;
import com.kyratsous.runnersapp.services.ratings.DietRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@Controller
public class DietController {

    private final DietService dietService;
    private final UserService userService;
    private final DietRatingService dietRatingService;
    private final DietFavoriteService dietFavoriteService;

    public DietController(DietService dietService, UserService userService, DietRatingService dietRatingService, DietFavoriteService dietFavoriteService) {
        this.dietService = dietService;
        this.userService = userService;
        this.dietRatingService = dietRatingService;
        this.dietFavoriteService = dietFavoriteService;
    }

    @RequestMapping("/diets")
    public String showDiets(Model model) {
        model.addAttribute("diets", dietService.findAll());
        model.addAttribute("favorites", dietFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()));

        return "diets/index";
    }

    @RequestMapping("/diets/{id}")
    public String showDiet(@PathVariable Long id, Model model) {
        model.addAttribute("diet", dietService.findById(id));
        model.addAttribute("ratings", dietRatingService.findAllByDietId(id));
        model.addAttribute("newRating", new DietRating());
        model.addAttribute("isFavorite", dietFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(id));

        return "diets/show";
    }

    @RequestMapping("/my-diets")
    public String showMyDiets(Model model) {
        model.addAttribute("diets", dietService.findAllByUser(userService.getCurrentUser()));

        return "diets/my-diets";
    }

    @RequestMapping("/my-diets/new")
    public String createDietForm(Model model) {
        Diet diet = new Diet();
        model.addAttribute("diet", diet);

        return "diets/new";
    }

    @PostMapping("/my-diets/new")
    public String createDiet(@ModelAttribute("diet") Diet diet) {
        diet.setNutritionist(userService.getCurrentUser());
        diet.setDate(new Date());

        dietService.save(diet);
        return "redirect:/my-diets";
    }

    @RequestMapping("/my-diets/{id}/update")
    public String updateDietForm(@PathVariable Long id, Model model) {
        Diet diet = dietService.findById(id);

        if (!Objects.equals(userService.getCurrentUser().getId(), diet.getNutritionist().getId())) {
            return "errors/error-403";
        }

        model.addAttribute("diet", diet);

        return "diets/update";
    }

    @PostMapping("/my-diets/update")
    public String updateDiet(@ModelAttribute("diet") Diet diet) {
        dietService.update(diet);
        return "redirect:/my-diets";
    }

    @GetMapping("/my-diets/{id}/delete")
    public String deleteDiet(@PathVariable Long id) {
        dietService.deleteById(id);
        return "redirect:/my-diets";
    }

    @PostMapping("/diets/{diet_id}/rating")
    public String addDietRating(@PathVariable Long diet_id, @ModelAttribute DietRating dietRating) {
        dietRating.setUser(userService.getCurrentUser());
        dietRating.setDiet(dietService.findById(diet_id));

        dietRatingService.save(dietRating);

        return "redirect:/diets/" + diet_id;
    }

    @GetMapping("/diets/{id}/rating/{rate_id}/delete")
    public String deleteDietRating(@PathVariable Long id, @PathVariable Long rate_id) {
        dietRatingService.deleteById(rate_id);

        return "redirect:/diets/" + id;
    }

    @RequestMapping("/diets/favorites")
    public String showFavoriteDiets(Model model) {
        model.addAttribute("diets",
                dietService.findAll().stream().filter(diet ->
                        dietFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(diet.getId())).toArray());

        return "diets/favorites";
    }

    @GetMapping("/diets/{id}/add-favorite")
    @ResponseBody
    public String addDietToFavorites(@PathVariable Long id) {
        dietFavoriteService.save(new DietFavorite(dietService.findById(id), userService.getCurrentUser()));
        return "ok";
    }

    @GetMapping("/diets/{id}/remove-favorite")
    @ResponseBody
    public String removeDietFromFavorites(@PathVariable Long id) {
        dietFavoriteService.deleteByObjectId(id, userService.getCurrentUser());
        return "ok";
    }
}
