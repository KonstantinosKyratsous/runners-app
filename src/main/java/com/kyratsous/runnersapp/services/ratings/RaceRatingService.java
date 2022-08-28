package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.RaceRating;
import com.kyratsous.runnersapp.repositories.ratings.RaceRatingRepository;
import com.kyratsous.runnersapp.services.CrudService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class RaceRatingService implements CrudService<RaceRating, Long> {
    private final RaceRatingRepository raceRatingRepository;

    public RaceRatingService(RaceRatingRepository raceRatingRepository) {
        this.raceRatingRepository = raceRatingRepository;
    }

    @Override
    public Set<RaceRating> findAll() {
        Set<RaceRating> raceRatings = new HashSet<>();
        raceRatingRepository.findAll().forEach(raceRatings::add);
        return raceRatings;
    }

    @Override
    public RaceRating findById(Long id) {
        return raceRatingRepository.findById(id).orElse(null);
    }

    @Override
    public RaceRating save(RaceRating raceRating) {
        return raceRatingRepository.save(raceRating);
    }

    @Override
    public void deleteById(Long id) {
        if (isCurrentUserOwnerOfRating(findById(id).getUser()))
            raceRatingRepository.deleteById(id);
    }

    @Override
    public void update(RaceRating raceRating) {
        RaceRating currentRaceRating = findById(raceRating.getId());

        if (!isCurrentUserOwnerOfRating(currentRaceRating.getUser()))
            return;

        currentRaceRating.setDescription(raceRating.getDescription());
        currentRaceRating.setRate(raceRating.getRate());

        raceRatingRepository.save(currentRaceRating);
    }

    @Override
    public Set<RaceRating> findAllByUser(User user) {
        return raceRatingRepository.findAllByUser(user);
    }

    public Set<RaceRating> findAllByRaceId(Long id) {
        return raceRatingRepository.findAllByRaceId(id);
    }

    private boolean isCurrentUserOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getName(), user.getUsername());
    }
}
