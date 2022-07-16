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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class RaceController {

    private final RaceService raceService;
    private final UserService userService;

    public RaceController(RaceService raceService, UserService userService) {
        this.raceService = raceService;
        this.userService = userService;
    }

    @RequestMapping("/races")
    public String getAllRaces(Model model, @RequestParam(required=false) Map<String,String> filters) {
        model.addAttribute("races", filters.isEmpty()? raceService.findAll(): raceService.findFilteredRaces(filters));
        model.addAttribute("locations", raceService.findRaceLocations());
        model.addAttribute("format", new SimpleDateFormat("dd/MM/yyyy"));
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
    public String createRace(@ModelAttribute("race") Race race, @RequestParam("dateString") String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        race.setDate(format.parse(date));

        String link = correctLinkToBuyTickets(race.getLinkToBuyTickets());
        race.setLinkToBuyTickets(link);
        race.setOrganizer(userService.getCurrentUser());

        raceService.save(race);
        return "redirect:/my-races";
    }

    @RequestMapping("/my-races/{id}/update")
    public String updateRaceForm(@PathVariable Long id, Model model) {
        Race race = raceService.findById(id);
        model.addAttribute("race", race);
        model.addAttribute("format", new SimpleDateFormat("yyyy-MM-dd"));
        return "races/update";
    }

    @PostMapping("/my-races/{id}/update")
    public String updateRace(@PathVariable Long id, @ModelAttribute("race") Race race, @RequestParam("dateString") String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        race.setDate(format.parse(date));

        String link = correctLinkToBuyTickets(race.getLinkToBuyTickets());
        race.setLinkToBuyTickets(link);
        raceService.update(race);
        return "redirect:/my-races";
    }

    @GetMapping("/my-races/{id}/delete")
    public String deleteRace(@PathVariable Long id) {
        raceService.deleteById(id);
        return "redirect:/my-races";
    }

    @RequestMapping("/races/{id}")
    public String showRace(@PathVariable Long id, Model model) {
        model.addAttribute("race", raceService.findById(id));

        return "races/show";
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

    private String correctLinkToBuyTickets(String linkToBuyTickets) {
        if (linkToBuyTickets.isEmpty())
            return linkToBuyTickets;

        String correctLink = "";

        if (!linkToBuyTickets.contains("https")) {
            correctLink = "https://";
        }

        if (!linkToBuyTickets.contains("www")) {
            correctLink += "www.";
        }
        //correctLink += linkToBuyTickets;
        return correctLink + linkToBuyTickets;
    }
}
