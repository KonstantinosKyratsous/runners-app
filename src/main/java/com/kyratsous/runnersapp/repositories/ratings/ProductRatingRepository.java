package com.kyratsous.runnersapp.repositories.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ProductRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductRatingRepository extends CrudRepository<ProductRating, Long> {
    Set<ProductRating> findAllByProductId(Long id);

    Set<ProductRating> findAllByUser(User user);
}
