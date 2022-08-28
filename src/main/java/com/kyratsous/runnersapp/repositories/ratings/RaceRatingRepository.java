package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.RaceRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RaceRatingRepository extends CrudRepository<RaceRating, Long> {
    Set<RaceRating> findAllByRaceId(Long id);

    Set<RaceRating> findAllByUser(User user);
}
