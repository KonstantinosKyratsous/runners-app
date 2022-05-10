package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.services.DietService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@Controller
public class DietController {

    private final DietService dietService;
    private final UserService userService;

    public DietController(DietService dietService, UserService userService) {
        this.dietService = dietService;
        this.userService = userService;
    }

    @RequestMapping("/diets")
    public String showDiets(Model model) {
        model.addAttribute("diets", dietService.findAll());

        return "diets/index";
    }

    @RequestMapping("/my-diets")
    public String showMyDiets(Model model) {
        model.addAttribute("diets", dietService.findAllByUserId(userService.getCurrentUser()));

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

        //Change the format
        Date date = new Date();
        diet.setDate(date.toString());

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

    @PostMapping("/my-diets/{id}/update")
    public String updateDiet(@PathVariable Long id, @ModelAttribute("diet") Diet diet) {
        dietService.update(diet);
        return "redirect:/my-diets";
    }

    @GetMapping("/my-diets/{id}/delete")
    public String deleteDiet(@PathVariable Long id) {
        dietService.deleteById(id);
        return "redirect:/my-diets";
    }
}
