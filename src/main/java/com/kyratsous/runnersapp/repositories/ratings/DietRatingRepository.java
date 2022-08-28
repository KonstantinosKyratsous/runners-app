package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.DietRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DietRatingRepository extends CrudRepository<DietRating, Long> {

    Set<DietRating> findAllByDietId(Long id);

    Set<DietRating> findAllByUser(User user);
}
