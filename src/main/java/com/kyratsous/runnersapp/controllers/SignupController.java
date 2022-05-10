package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Authority;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.AuthorityService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignupController {

    private final UserService userService;
    private final AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SignupController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @RequestMapping({"/signup", "/signup.html"})
    public String signup(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        Authority authority = new Authority();
        model.addAttribute("authority", authority);

        return "signup";
    }

    @PostMapping("/process-signup")
    public String processSignup(@ModelAttribute("user") User user, @ModelAttribute("authority") Authority authority) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.save(user);

        authority.setUser(user);
        authorityService.save(authority);

        return "redirect:/login";
    }
}
