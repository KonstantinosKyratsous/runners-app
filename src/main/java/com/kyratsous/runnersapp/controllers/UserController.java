package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.DietService;
import com.kyratsous.runnersapp.services.ExerciseService;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final RaceService raceService;
    private final ExerciseService exerciseService;
    private final DietService dietService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RaceService raceService, ExerciseService exerciseService, DietService dietService) {
        this.userService = userService;
        this.raceService = raceService;
        this.exerciseService = exerciseService;
        this.dietService = dietService;
    }

    @RequestMapping({"/user", "/user.html", "/profile"})
    public String user(Model model) {
        User user = userService.getCurrentUser();

        model.addAttribute("user", user);
        model.addAttribute("races", raceService.findAllByUserId(user));
        model.addAttribute("exercises", exerciseService.findAllByUserId(user));
        model.addAttribute("diets", dietService.findAllByUserId(user));

        return "user/user";
    }

    @GetMapping("/user/update")
    public String updateUser(Model model) {
        model.addAttribute("user", userService.getCurrentUser());

        return "user/user-update";
    }

    @PostMapping("/user/update")
    public String processUpdateUser(@ModelAttribute("user") User user) {
        userService.update(user);

        return "redirect:/user";
    }

    @GetMapping("/user/change_password")
    public String updatePassword() {
        return "user/password-update";
    }

    @PostMapping("/user/change_password")
    public String processUpdatePassword(@ModelAttribute("current_password") String currentPassword, @ModelAttribute("new_password") String newPassword) {
        User user = userService.getCurrentUser();

        String encodedPassword = passwordEncoder.encode(currentPassword);
        if (passwordEncoder.matches(encodedPassword, user.getPassword())) {
            return "redirect:/user/change_password/error";
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userService.updatePassword(user);

        return "redirect:/user";
    }

    @GetMapping("/user/change_password/error")
    public String updatePasswordWithError(Model model) {
        model.addAttribute("updateError", true);

        return "user/password-update";
    }
}
