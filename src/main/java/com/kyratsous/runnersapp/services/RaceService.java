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
public class RaceService implements CrudService<Race, Long> {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
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
    public Race save(Race race) {
        return raceRepository.save(race);
    }

    @Override
    public void deleteById(Long id) {
        if (isNotCurrentUser(findById(id).getOrganizer()))
            return;

        raceRepository.deleteById(id);
    }

    @Override
    public void update(Race race) {
        Race currentRace = findById(race.getId());

        if (isNotCurrentUser(currentRace.getOrganizer()))
            return;

        currentRace.setTitle(race.getTitle());
        currentRace.setDate(race.getDate());
        currentRace.setDistanceOptions(race.getDistanceOptions());
        currentRace.setFieldOptions(race.getFieldOptions());
        currentRace.setLatitude(race.getLatitude());
        currentRace.setLongitude(race.getLongitude());
        currentRace.setLocation(race.getLocation());
        currentRace.setPrice(race.getPrice());
        currentRace.setDescription(race.getDescription());
        if (race.getFile() != null)
            currentRace.setFile(race.getFile());
        currentRace.setRegistrationLink(race.getRegistrationLink());

        raceRepository.save(currentRace);
    }

    @Override
    public Set<Race> findAllByUser(User user) {
        return (user != null)? raceRepository.findAllByOrganizer(user): new HashSet<>();
    }

    public Set<Race> findFilteredRaces(Map<String, String> filters) {
        Set<Race> results = new HashSet<>();

        if (filters.get("sort") != null) {
            if (filters.get("sort").equals("most-recent")) {
                results = raceRepository.findAllByOrderByDateAsc();
            }
            else if (filters.get("sort").equals("least-recent")) {
                results = raceRepository.findAllByOrderByDateDesc();
            }
        }
        else {
            results = findAll();
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
            results.retainAll(raceRepository.findAllByDateBetween(startDate, endDate));
        }

        if (filters.get("distance") != null) {
            Set<String> types = new HashSet<>(Arrays.asList(filters.get("distance").split(",")));
            results.retainAll(raceRepository.findAllByDistanceOptionsIsIn(types));
        }

        if (filters.get("field") != null) {
            Set<String> fields = new HashSet<>(Arrays.asList(filters.get("field").split(",")));
            results.retainAll(raceRepository.findAllByFieldOptionsIsIn(fields));
        }

        return results;
    }

    private boolean isNotCurrentUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
