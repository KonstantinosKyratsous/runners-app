package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Race;
import org.springframework.data.repository.CrudRepository;

public interface RaceRepository extends CrudRepository<Race, Long> {
}
