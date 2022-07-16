package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Race;

import java.util.Map;
import java.util.Set;

public interface RaceService extends CrudService<Race, Long>{

    Set<String> findRaceLocations();

    Set<Race> findFilteredRaces(Map<String, String> filters);
}
