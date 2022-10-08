package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final RaceService raceService;
    private final TrainingPlanService trainingPlanService;
    private final DietService dietService;
    private final ProductService productService;
    private final NotificationService notificationService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RaceService raceService, TrainingPlanService trainingPlanService, DietService dietService, ProductService productService, NotificationService notificationService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.raceService = raceService;
        this.trainingPlanService = trainingPlanService;
        this.dietService = dietService;
        this.productService = productService;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping({"/user/{username}", "/profile"})
    public String showProfile(@PathVariable(required = false) String username, Model model, @RequestParam(required = false) Map<String, String> filter) {
        if (username == null)
            username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (filter != null && !filter.isEmpty()) {
            switch (filter.get("p")) {
                case "races":
                    model.addAttribute("races", raceService.findAllByUser(userService.findByUsername(username)));
                    break;
                case "training-plans":
                    model.addAttribute("trainingPlans", trainingPlanService.findAllByUser(userService.findByUsername(username)));
                    break;
                case "diets":
                    model.addAttribute("diets", dietService.findAllByUser(userService.findByUsername(username)));
                    break;
                case "products":
                    model.addAttribute("products", productService.findAllByUser(userService.findByUsername(username)));
                    break;
            }
        }
        model.addAttribute("notifications", notificationService.findAllByUser(userService.getCurrentUser()));
        model.addAttribute("user", userService.findByUsername(username));

        return "user/public-profile";
    }

    @GetMapping("/user/update")
    public String updateUser(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);

        return "user/user-update";
    }

    @PostMapping("/user/update")
    public String processUpdateUser(@ModelAttribute("user") User user) {
        userService.update(user);

        return "redirect:/profile";
    }

    @GetMapping("/user/change_password")
    public String updatePassword() {
        return "user/password-update";
    }

    @PostMapping("/user/change_password")
    public String processUpdatePassword(@ModelAttribute("current_password") CharSequence currentPassword, @ModelAttribute("new_password") String newPassword) {
        User user = userService.getCurrentUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            return "redirect:/user/change_password/error";

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userService.updatePassword(user);

        return "redirect:/profile";
    }

    @GetMapping("/user/change_password/error")
    public String updatePasswordWithError(Model model) {
        model.addAttribute("updateError", true);

        return "user/password-update";
    }
}
