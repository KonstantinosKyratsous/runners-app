package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.RaceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RaceServiceImpl implements RaceService{

    private final RaceRepository raceRepository;

    public RaceServiceImpl(RaceRepository raceRepository, UserService userService) {
        this.raceRepository = raceRepository;
    }

    @Override
    public Set<Race> findAll() {
        Set<Race> races = new HashSet<>();
        raceRepository.findAll().forEach(races::add);
        return races;
    }

    @Override
    public Race findById(Long id) {
        return raceRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Race race) {
        raceRepository.save(race);
    }

    @Override
    public void delete(Race race) {
        raceRepository.delete(race);
    }

    @Override
    public void deleteById(Long id) {
        if (!isOwnerOfRace(findById(id).getOrganizer())) {
            System.out.println("You cannot delete this race. You are not the owner of this race post. ");
            return;
        }
        raceRepository.deleteById(id);
    }

    @Override
    public void update(Race race) {
        Race currentRace = findById(race.getId());

        if (!isOwnerOfRace(currentRace.getOrganizer())) {
            System.out.println("You cannot update this race. You are not the owner of this race post. ");
            return;
        }

        currentRace.setTitle(race.getTitle());
        currentRace.setDate(race.getDate());
        currentRace.setLocation(race.getLocation());
        currentRace.setPrice(race.getPrice());
        currentRace.setDescription(race.getDescription());
        currentRace.setLinkToBuyTickets(race.getLinkToBuyTickets());

        raceRepository.save(currentRace);
    }

    @Override
    public Set<Race> findAllByUserId(User user) {
        Set<Race> races = new HashSet<>();

        if(user != null) {
            for (Race race : findAll()) {
                if (Objects.equals(race.getOrganizer().getId(), user.getId())) {
                    races.add(race);
                }
            }
        }

        return races;
    }

    @Override
    public Set<String> findRaceLocations() {
        Set<Race> races = findAll();
        Set<String> locations = new HashSet<>();

        if (!races.isEmpty()) {
            for (Race race: races) {
                if (!locations.contains(race.getLocation())) {
                    locations.add(race.getLocation());
                }
            }
        }

        return locations;
    }

    @Override
    public Set<Race> findFilteredRaces(Map<String, String> filters) {
        Set<Race> results = new HashSet<>();
        Set<Race> resultsByLocation = new HashSet<>();
        Set<Race> resultsByDate = new HashSet<>();

        if (filters.get("location") != null) {
            String[] locations = filters.get("location").split(",");

            for (Race race: findAll())
                if (Arrays.asList(locations).contains(race.getLocation()))
                    resultsByLocation.add(race);
        }

        if (filters.get("date-range") != null) {
            String[] dates = filters.get("date-range").split(" - ");
            Date startDate;
            Date endDate;
            try {
                startDate = new SimpleDateFormat("MM/dd/yyyy").parse(dates[0]);
                endDate = new SimpleDateFormat("MM/dd/yyyy").parse(dates[1]);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            for (Race race: findAll()){
                if (race.getDate().after(startDate) && race.getDate().before(endDate))
                    resultsByDate.add(race);
            }
        }

        if (resultsByDate.isEmpty())
            return resultsByLocation;
        else if (resultsByLocation.isEmpty())
            return resultsByDate;
        else
            for (Race race: resultsByDate)
                if (resultsByLocation.contains(race))
                    results.add(race);

        return results;
    }

    private boolean isOwnerOfRace(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return Objects.equals(auth.getName(), user.getUsername());
    }
}
