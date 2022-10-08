package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping({"/signup", "/signup.html"})
    public String signup(@RequestParam(value = "error", defaultValue = "none") String signupError, Model model) {
        model.addAttribute("user", new User());

        if (signupError.equals("username"))
            model.addAttribute("errorMessage", "The username already exists");
        return "user/signup";
    }

    @PostMapping("/process-signup")
    public String processSignup(@ModelAttribute("user") User user) {
        if (userService.findByUsername(user.getUsername()) != null)
            return "redirect:/signup?error=username";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

        return "redirect:/login";
    }
}
