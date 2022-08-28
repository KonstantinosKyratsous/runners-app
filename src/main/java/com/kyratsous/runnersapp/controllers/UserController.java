package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.config.MailConfig;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.*;
import com.kyratsous.runnersapp.services.favorites.DietFavoriteService;
import com.kyratsous.runnersapp.services.favorites.ExerciseFavoriteService;
import com.kyratsous.runnersapp.services.favorites.ProductFavoriteService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final RaceService raceService;
    private final ExerciseService exerciseService;
    private final DietService dietService;
    private final ProductService productService;
    private final RaceFavoriteService raceFavoriteService;
    private final ExerciseFavoriteService exerciseFavoriteService;
    private final DietFavoriteService dietFavoriteService;
    private final ProductFavoriteService productFavoriteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RaceService raceService, ExerciseService exerciseService, DietService dietService, ProductService productService, RaceFavoriteService raceFavoriteService, ExerciseFavoriteService exerciseFavoriteService, DietFavoriteService dietFavoriteService, ProductFavoriteService productFavoriteService) {
        this.userService = userService;
        this.raceService = raceService;
        this.exerciseService = exerciseService;
        this.dietService = dietService;
        this.productService = productService;
        this.raceFavoriteService = raceFavoriteService;
        this.exerciseFavoriteService = exerciseFavoriteService;
        this.dietFavoriteService = dietFavoriteService;
        this.productFavoriteService = productFavoriteService;
    }

    @RequestMapping("/profile")
    public String user(Model model) {
        User user = userService.getCurrentUser();

        model.addAttribute("user", user);
        model.addAttribute("races", raceService.findAllByUser(user));
        model.addAttribute("exercises", exerciseService.findAllByUser(user));
        model.addAttribute("diets", dietService.findAllByUser(user));
        model.addAttribute("products", productService.findAllByUser(user));

        return "user/profile";
    }

    @RequestMapping("/user/{username}")
    public String showPublicProfile(@PathVariable String username, Model model, @RequestParam(required=false) Map<String,String> filter) {

        if (!filter.isEmpty()) {
            switch (filter.get("p")) {
                case "races":
                    model.addAttribute("races", raceService.findAllByUser(userService.findByUsername(username)));
                    break;
                case "exercises":
                    model.addAttribute("exercises", exerciseService.findAllByUser(userService.findByUsername(username)));
                    break;
                case "diets":
                    model.addAttribute("diets", dietService.findAllByUser(userService.findByUsername(username)));
                    break;
                case "products":
                    model.addAttribute("products", productService.findAllByUser(userService.findByUsername(username)));
                    break;
            }
        }

        model.addAttribute("user", userService.findByUsername(username));

        return "user/public-profile";
    }

    @RequestMapping("/user/update")
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
    public String processUpdatePassword(@ModelAttribute("current_password") String currentPassword, @ModelAttribute("new_password") String newPassword) {
        User user = userService.getCurrentUser();

        String encodedPassword = passwordEncoder.encode(currentPassword);
        if (passwordEncoder.matches(encodedPassword, user.getPassword())) {
            return "redirect:/user/change_password/error";
        }

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

    @RequestMapping("/favorites")
    public String showFavorites(Model model) {
        model.addAttribute("races",
                raceService.findAll().stream().filter(race ->
                        raceFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(race.getId())).toArray());
        model.addAttribute("exercises",
                exerciseService.findAll().stream().filter(exercise ->
                        exerciseFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(exercise.getId())).toArray());
        model.addAttribute("diets",
                dietService.findAll().stream().filter(diet ->
                        dietFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(diet.getId())).toArray());
        model.addAttribute("products",
                productService.findAll().stream().filter(product ->
                        productFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(product.getId())).toArray());

        return "user/favorites";
    }

    @RequestMapping("/user/admin/sendmail")
    public String sendMail() throws MessagingException {
        String text = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>User</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h3>Test</h3>\n" +
                "</body>\n" +
                "</html>";

        JavaMailSender sender = MailConfig.getMailConfig();
        // Set Mime Message:
        MimeMessage message = sender.createMimeMessage();
        // Set Mime Message Helper:
        MimeMessageHelper htmlMessage = new MimeMessageHelper(message, true);

        // Set Mail Attributes / Properties:
        htmlMessage.setTo("kyratsous26@gmail.com");
        htmlMessage.setFrom("user@runnersapp.com");
        htmlMessage.setSubject("subject");
        htmlMessage.setText(text, true);
        // Send Message:
        sender.send(message);

        return "redirect:/user/admin";
    }
}
