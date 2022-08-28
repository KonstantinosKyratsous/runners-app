package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ExerciseRating;
import com.kyratsous.runnersapp.repositories.ratings.ExerciseRatingRepository;
import com.kyratsous.runnersapp.services.CrudService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ExerciseRatingService implements CrudService<ExerciseRating, Long> {
    private final ExerciseRatingRepository exerciseRatingRepository;

    public ExerciseRatingService(ExerciseRatingRepository exerciseRatingRepository) {
        this.exerciseRatingRepository = exerciseRatingRepository;
    }

    @Override
    public Set<ExerciseRating> findAll() {
        Set<ExerciseRating> exerciseRatings = new HashSet<>();
        exerciseRatingRepository.findAll().forEach(exerciseRatings::add);
        return exerciseRatings;
    }

    @Override
    public ExerciseRating findById(Long id) {
        return exerciseRatingRepository.findById(id).orElse(null);
    }

    @Override
    public ExerciseRating save(ExerciseRating rating) {
        return exerciseRatingRepository.save(rating);
    }

    @Override
    public void deleteById(Long id) {
        if (isCurrentUserOwnerOfRating(findById(id).getUser()))
            exerciseRatingRepository.deleteById(id);
    }

    @Override
    public void update(ExerciseRating rating) {
        ExerciseRating currentExerciseRating = findById(rating.getId());

        if (!isCurrentUserOwnerOfRating(currentExerciseRating.getUser()))
            return;

        currentExerciseRating.setRate(rating.getRate());
        currentExerciseRating.setDescription(rating.getDescription());

        exerciseRatingRepository.save(currentExerciseRating);
    }

    @Override
    public Set<ExerciseRating> findAllByUser(User user) {
        return exerciseRatingRepository.findAllByUser(user);
    }

    public Set<ExerciseRating> findAllByExerciseId(Long id) {
        return exerciseRatingRepository.findAllByExerciseId(id);
    }

    private boolean isCurrentUserOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getName(), user.getUsername());
    }
}
