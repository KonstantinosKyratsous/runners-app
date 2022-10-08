package com.kyratsous.runnersapp.schedule;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ProductFavorite;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
import com.kyratsous.runnersapp.services.NotificationService;
import com.kyratsous.runnersapp.services.ProductService;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.ProductFavoriteService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NotificationCreator {

    private final NotificationService notificationService;
    private final RaceFavoriteService raceFavoriteService;
    private final ProductFavoriteService productFavoriteService;
    private final UserService userService;
    private final ProductService productService;
    private final RaceService raceService;

    public NotificationCreator(NotificationService notificationService, RaceFavoriteService raceFavoriteService, ProductFavoriteService productFavoriteService, UserService userService, ProductService productService, RaceService raceService) {
        this.notificationService = notificationService;
        this.raceFavoriteService = raceFavoriteService;
        this.productFavoriteService = productFavoriteService;
        this.userService = userService;
        this.productService = productService;
        this.raceService = raceService;
    }

    @Scheduled(cron = "0 0 16 * * ?") // 4PM every day
    public void create() {
        // Create notifications for races that the user have in favorites
        for (RaceFavorite favorite: raceFavoriteService.findAll()) {
            // 7 days left
            if (addDaysToDate(new Date(), 7).before(favorite.getRace().getDate()) &&
                    addDaysToDate(new Date(), 6).after(favorite.getRace().getDate())) {
                notificationService.save(
                        new Notification("Less than 7 days: " + favorite.getRace().getTitle(),
                                favorite.getRace().getDescription(), new Date(), "/races/" + favorite.getRace().getId(), favorite.getUser()));
            }

            // One month left
            if (addDaysToDate(new Date(), 30).before(favorite.getRace().getDate()) &&
                    addDaysToDate(new Date(), 29).after(favorite.getRace().getDate())) {
                notificationService.save(
                        new Notification("This month: " + favorite.getRace().getTitle(),
                                favorite.getRace().getDescription(), new Date(), "/races/" + favorite.getRace().getId(), favorite.getUser()));
            }
        }

        // Create Notifications for products similar to user's favorite products
        for (User user: userService.findAll()) {
            Set<String> favTypes = new HashSet<>();
            Set<Long> favProductIds = new HashSet<>();

            for (ProductFavorite favorite: productFavoriteService.findAll()) {
                favTypes.add(favorite.getProduct().getType());
                favProductIds.add(favorite.getProduct().getId());
            }

            for (Product product: productService.findAll()) {
                if (favTypes.contains(product.getType()) && !favProductIds.contains(product.getId())) {
                    if (!notificationService.existsByTitle("Recommended Product: " + product.getName()))
                        notificationService.save(
                                new Notification("Recommended Product: " + product.getName(), product.getDescription(), new Date(), "/products/" + product.getId(), user)
                        );
                }
            }
        }

        // Create Notifications for races that the day of the race has passed
        for (Race race: raceService.findAll()) {
            if (race.getDate().before(new Date()) && race.getDate().after(addDaysToDate(new Date(), 1))) {
                String title = race.getTitle().length() > 240? race.getTitle().substring(0, 240): race.getTitle();
                notificationService.save(new Notification("Past Race: " + title,
                                                         "Click here to delete/update it. ", new Date(), "/my-races", race.getOrganizer()));
            }
        }

        System.out.println("Notifications created!");
    }

    private static Date addDaysToDate(final Date date, int noOfDays) {
        Date newDate = new Date(date.getTime());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, noOfDays);
        newDate.setTime(calendar.getTime().getTime());

        return newDate;
    }
}
