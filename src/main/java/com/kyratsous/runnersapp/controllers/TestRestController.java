package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
    private final RaceFavoriteService raceFavoriteService;
    private final RaceService raceService;
    private final UserService userService;

    public TestRestController(RaceFavoriteService raceFavoriteService, RaceService raceService, UserService userService) {
        this.raceFavoriteService = raceFavoriteService;
        this.raceService = raceService;
        this.userService = userService;
    }

//    @GetMapping("/races/{id}/add-favorite")
//    public void addToFavorites(@PathVariable Long id, HttpServletRequest request) {
//        raceFavoriteService.save(new RaceFavorite(raceService.findById(id), userService.getCurrentUser()));
//    }
}
