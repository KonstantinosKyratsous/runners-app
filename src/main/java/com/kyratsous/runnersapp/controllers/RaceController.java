package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RaceController {

    private final RaceService raceService;
    private final UserService userService;

    public RaceController(RaceService raceService, UserService userService) {
        this.raceService = raceService;
        this.userService = userService;
    }

    @RequestMapping("/races")
    public String getAllRaces(Model model) {
        model.addAttribute("races", raceService.findAll());
        model.addAttribute("locations", raceService.findRaceLocations());

        return "races/index";
    }

    @RequestMapping("/my-races")
    public String getMyRaces(Model model) {
        model.addAttribute("races", raceService.findAllByUserId(userService.getCurrentUser()));

        return "races/my-races";
    }

    @RequestMapping("/my-races/new")
    public String createRaceForm(Model model) throws IOException, URISyntaxException {
        Race race = new Race();
        model.addAttribute("race", race);
        model.addAttribute("cities", getCities());

        return "races/new";
    }

    @PostMapping("/my-races/new")
    public String createRace(@ModelAttribute("race") Race race) {

        race.setOrganizer(userService.getCurrentUser());

        raceService.save(race);
        return "redirect:/my-races";
    }

    @RequestMapping("/my-races/{id}/update")
    public String updateRaceForm(@PathVariable Long id, Model model) {
        Race race = raceService.findById(id);
        model.addAttribute("race", race);

        return "races/update";
    }

    @PostMapping("/my-races/{id}/update")
    public String updateRace(@PathVariable Long id, @ModelAttribute("race") Race race) {
        raceService.update(race);
        return "redirect:/my-races";
    }

    @GetMapping("/my-races/{id}/delete")
    public String deleteRace(@PathVariable Long id) {
        raceService.deleteById(id);
        return "redirect:/my-races";
    }

    private List<String> getCities() throws IOException, URISyntaxException {
        List<String> cities = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("static/resources/text/cities.txt");

        assert resource != null;
        File file = new File(resource.toURI());

        BufferedReader br = new BufferedReader(new FileReader(file));

        String city;
        while ((city = br.readLine()) != null)
            cities.add(city);

        return cities;
    }
}
