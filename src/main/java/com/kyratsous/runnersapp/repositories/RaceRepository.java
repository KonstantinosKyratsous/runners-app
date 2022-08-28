package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Set;

public interface RaceRepository extends CrudRepository<Race, Long> {

    Set<Race> findAllByOrganizer(User user);

    Set<Race> findAllByOrderByDateAsc();

    Set<Race> findAllByOrderByDateDesc();

    Set<Race> findAllByDateBetween(Date before, Date after);

    Set<Race> findAllByDistanceOptionsIsIn(Set<String> distanceOptions);

    Set<Race> findAllByFieldOptionsIsIn(Set<String> fieldOptions);
}
