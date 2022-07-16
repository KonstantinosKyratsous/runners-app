package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.DietRating;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.DietRatingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DietRatingServiceImpl implements DietRatingService {
    private final DietRatingRepository dietRatingRepository;

    public DietRatingServiceImpl(DietRatingRepository dietRatingRepository) {
        this.dietRatingRepository = dietRatingRepository;
    }

    @Override
    public Set<DietRating> findAll() {
        Set<DietRating> ratings = new HashSet<>();
        dietRatingRepository.findAll().forEach(ratings::add);
        return ratings;
    }

    @Override
    public DietRating findById(Long id) {
        return dietRatingRepository.findById(id).orElse(null);
    }

    @Override
    public void save(DietRating rating) {
        dietRatingRepository.save(rating);
    }

    @Override
    public void delete(DietRating rating) {
        if (isOwnerOfRating(rating.getUser())) {
            dietRatingRepository.delete(rating);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isOwnerOfRating(findById(id).getUser())) {
            dietRatingRepository.deleteById(id);
        }
    }

    @Override
    public void update(DietRating rating) {
        if (isOwnerOfRating(rating.getUser())) {
          DietRating currentDietRating = findById(rating.getId());

          currentDietRating.setRate(rating.getRate());
          currentDietRating.setDescription(rating.getDescription());
          currentDietRating.setUpdated(true);

          dietRatingRepository.save(currentDietRating);
        }
    }

    @Override
    public Set<DietRating> findAllByUserId(User user) {
        Set<DietRating> ratings = new HashSet<>();

        for (DietRating rating: findAll()) {
            if (Objects.equals(rating.getUser(), user)) {
                ratings.add(rating);
            }
        }

        return ratings;
    }

    @Override
    public Set<DietRating> findAllByDietId(Long id) {
        Set<DietRating> ratings = new HashSet<>();

        for (DietRating rating: findAll()) {
            if (Objects.equals(rating.getDiet().getId(), id)) {
                ratings.add(rating);
            }
        }

        return ratings;
    }

    private boolean isOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getName(), user.getUsername());
    }
}
