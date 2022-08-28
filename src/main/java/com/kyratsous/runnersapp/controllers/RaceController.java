package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
import com.kyratsous.runnersapp.model.ratings.RaceRating;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import com.kyratsous.runnersapp.services.ratings.RaceRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private final RaceRatingService raceRatingService;
    private final RaceFavoriteService raceFavoriteService;

    public RaceController(RaceService raceService, UserService userService, RaceRatingService raceRatingService, RaceFavoriteService raceFavoriteService) {
        this.raceService = raceService;
        this.userService = userService;
        this.raceRatingService = raceRatingService;
        this.raceFavoriteService = raceFavoriteService;
    }

    @RequestMapping("/races")
    public String getAllRaces(Model model, @RequestParam(required=false) Map<String,String> filters,
                              @RequestParam(value = "pref", defaultValue = "false") boolean pref,
                              @CookieValue(value = "VIEW-AS", defaultValue = "GUEST") String type) {
        User currentUser = userService.getCurrentUser();

        if (currentUser != null && pref) {
            return "redirect:/races?" + currentUser.getPreferencesToString(true, true, false);
        }

        model.addAttribute("races", filters.isEmpty()? raceService.findAll(): raceService.findFilteredRaces(filters));
        model.addAttribute("favorites", raceFavoriteService.findAllObjectIdsByUser(currentUser));

        return "races/index";
    }

    @RequestMapping("/my-races")
    public String getMyRaces(Model model) {
        model.addAttribute("races", raceService.findAllByUser(userService.getCurrentUser()));

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
    public String createRace(@ModelAttribute("race") Race race,
                             @RequestParam("dateString") String date,
                             @RequestParam("file-pdf") MultipartFile file) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        race.setDate(format.parse(date));
        race.setFile(file.getBytes());

        String link = correctLinkToBuyTickets(race.getRegistrationLink());
        race.setRegistrationLink(link);
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

    @PostMapping("/my-races/update")
    public String updateRace(@ModelAttribute("race") Race race,
                             @RequestParam("dateString") String date,
                             @RequestParam("file-pdf") MultipartFile file) throws ParseException, IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        race.setDate(format.parse(date));
        if (!file.isEmpty())
            race.setFile(file.getBytes());

        String link = correctLinkToBuyTickets(race.getRegistrationLink());
        race.setRegistrationLink(link);
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
        model.addAttribute("isFavorite", raceFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(id));
        return "races/show";
    }

    @GetMapping("/races/{id}/documentation")
    public void showProductImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        Race race = raceService.findById(id);

        byte[] raceData = race.getFile();

        OutputStream o = response.getOutputStream();
        o.write(raceData);
        o.flush();
        o.close();
    }

    @PostMapping("/races/{race_id}/rating")
    public String addRaceRating(@PathVariable Long race_id, @ModelAttribute RaceRating raceRating) {
        raceRating.setUser(userService.getCurrentUser());
        raceRating.setRace(raceService.findById(race_id));

        raceRatingService.save(raceRating);

        return "redirect:/races/" + race_id;
    }

    @GetMapping("/races/{id}/rating/{rate_id}/delete")
    public String deleteRaceRating(@PathVariable Long id, @PathVariable Long rate_id) {
        raceRatingService.deleteById(rate_id);

        return "redirect:/races/" + id;
    }

    @RequestMapping("/races/favorites")
    public String showFavoriteRaces(Model model) {
        model.addAttribute("races",
                raceService.findAll().stream().filter(race ->
                        raceFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(race.getId())).toArray());

        return "races/favorites";
    }

    @GetMapping("/races/{id}/add-favorite")
    @ResponseBody
    public String addToFavorites(@PathVariable Long id) {
        raceFavoriteService.save(new RaceFavorite(raceService.findById(id), userService.getCurrentUser()));
        return "ok";
    }

    @GetMapping("/races/{id}/remove-favorite")
    @ResponseBody
    public String removeFromFavorites(@PathVariable Long id) {
        raceFavoriteService.deleteByObjectId(id, userService.getCurrentUser());
        return "ok";
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
