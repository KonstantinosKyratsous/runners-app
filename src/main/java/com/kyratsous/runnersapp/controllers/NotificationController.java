package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
import com.kyratsous.runnersapp.services.NotificationService;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final RaceService raceService;
    private final RaceFavoriteService raceFavoriteService;

    public NotificationController(NotificationService notificationService, UserService userService, RaceService raceService, RaceFavoriteService raceFavoriteService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.raceService = raceService;
        this.raceFavoriteService = raceFavoriteService;
    }

    @GetMapping("/notifications")
    @ResponseBody
    public Set<Notification> getNotifications() {
        return notificationService.findAllByUser(userService.getCurrentUser());
    }

    @GetMapping("/notifications/delete/{id}")
    @ResponseBody
    public void deleteNotification(@PathVariable Long id) {
        if (Objects.equals(userService.getCurrentUser(), notificationService.findById(id).getReceiver()))
            notificationService.deleteById(id);
    }

    @GetMapping("/notifications/delete-all")
    @ResponseBody
    public void deleteAllNotifications() {
        notificationService.deleteAllByReceiver(userService.getCurrentUser());
    }

    @RequestMapping("/notifications/send/{race_id}")
    public String sendNotificationForm(@PathVariable Long race_id, Model model) {
        model.addAttribute("notification", new Notification());
        return "notifications/send";
    }

    @PostMapping("/notifications/send/{race_id}")
    public String sendNotification(@ModelAttribute("notification") Notification notification,
                                   @PathVariable Long race_id) {
        Race race = raceService.findById(race_id);

        for (RaceFavorite favorite: raceFavoriteService.findAll()) {
            if (Objects.equals(favorite.getRace(), race)) {
                Notification notify = new Notification(notification.getTitle(), notification.getBody(), new Date(), "/races/" + race_id, favorite.getUser());
                notificationService.save(notify);
            }
        }

        return "redirect:/my-races";
    }
}
