package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.DietRating;
import com.kyratsous.runnersapp.repositories.ratings.DietRatingRepository;
import com.kyratsous.runnersapp.services.CrudService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DietRatingService implements CrudService<DietRating, Long> {
    private final DietRatingRepository dietRatingRepository;

    public DietRatingService(DietRatingRepository dietRatingRepository) {
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
    public DietRating save(DietRating rating) {
        return dietRatingRepository.save(rating);
    }

    @Override
    public void deleteById(Long id) {
        if (isCurrentUserOwnerOfRating(findById(id).getUser()))
            dietRatingRepository.deleteById(id);
    }

    @Override
    public void update(DietRating rating) {
        DietRating currentDietRating = findById(rating.getId());

        if (!isCurrentUserOwnerOfRating(currentDietRating.getUser()))
            return;

        currentDietRating.setRate(rating.getRate());
        currentDietRating.setDescription(rating.getDescription());

        dietRatingRepository.save(currentDietRating);

    }

    @Override
    public Set<DietRating> findAllByUser(User user) {
        return dietRatingRepository.findAllByUser(user);
    }

    public Set<DietRating> findAllByDietId(Long id) {
       return dietRatingRepository.findAllByDietId(id);
    }

    private boolean isCurrentUserOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getName(), user.getUsername());
    }
}
