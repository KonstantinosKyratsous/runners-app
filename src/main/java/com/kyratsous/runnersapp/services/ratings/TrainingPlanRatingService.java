package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.TrainingPlanRating;
import com.kyratsous.runnersapp.repositories.ratings.TrainingPlanRatingRepository;
import com.kyratsous.runnersapp.services.CrudService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class TrainingPlanRatingService implements CrudService<TrainingPlanRating, Long> {
    private final TrainingPlanRatingRepository trainingPlanRatingRepository;

    public TrainingPlanRatingService(TrainingPlanRatingRepository trainingPlanRatingRepository) {
        this.trainingPlanRatingRepository = trainingPlanRatingRepository;
    }

    @Override
    public Set<TrainingPlanRating> findAll() {
        Set<TrainingPlanRating> trainingPlanRatings = new HashSet<>();
        trainingPlanRatingRepository.findAll().forEach(trainingPlanRatings::add);
        return trainingPlanRatings;
    }

    @Override
    public TrainingPlanRating findById(Long id) {
        return trainingPlanRatingRepository.findById(id).orElse(null);
    }

    @Override
    public TrainingPlanRating save(TrainingPlanRating rating) {
        return trainingPlanRatingRepository.save(rating);
    }

    @Override
    public void deleteById(Long id) {
        if (isCurrentUserOwnerOfRating(findById(id).getUser()))
            trainingPlanRatingRepository.deleteById(id);
    }

    @Override
    public void update(TrainingPlanRating rating) {
        TrainingPlanRating currentTrainingPlanRating = findById(rating.getId());

        if (!isCurrentUserOwnerOfRating(currentTrainingPlanRating.getUser()))
            return;

        currentTrainingPlanRating.setRate(rating.getRate());
        currentTrainingPlanRating.setDescription(rating.getDescription());

        trainingPlanRatingRepository.save(currentTrainingPlanRating);
    }

    @Override
    public Set<TrainingPlanRating> findAllByUser(User user) {
        return trainingPlanRatingRepository.findAllByUser(user);
    }

    public Set<TrainingPlanRating> findAllByTrainingPlanId(Long id) {
        return trainingPlanRatingRepository.findAllByTrainingPlanId(id);
    }

    private boolean isCurrentUserOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getName(), user.getUsername());
    }
}
