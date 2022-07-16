package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.ExerciseRating;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ExerciseRatingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ExerciseRatingServiceImpl implements ExerciseRatingService {
    private final ExerciseRatingRepository exerciseRatingRepository;

    public ExerciseRatingServiceImpl(ExerciseRatingRepository exerciseRatingRepository) {
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
    public void save(ExerciseRating rating) {
        exerciseRatingRepository.save(rating);
    }

    @Override
    public void delete(ExerciseRating rating) {
        if (!isNotOwnerOfRating(rating.getUser()))
            exerciseRatingRepository.delete(rating);
    }

    @Override
    public void deleteById(Long id) {
        if (!isNotOwnerOfRating(findById(id).getUser()))
            exerciseRatingRepository.deleteById(id);
    }

    @Override
    public void update(ExerciseRating rating) {
        ExerciseRating currentExerciseRating = findById(rating.getId());

        if (isNotOwnerOfRating(rating.getUser())) {
            System.out.println("You cannot update this exercise rating. You are not the owner. ");
            return;
        }

        currentExerciseRating.setRate(rating.getRate());
        currentExerciseRating.setDescription(rating.getDescription());
        currentExerciseRating.setUpdated(true);

        exerciseRatingRepository.save(currentExerciseRating);
    }

    @Override
    public Set<ExerciseRating> findAllByUserId(User user) {
        Set<ExerciseRating> ratings = new HashSet<>();

        for (ExerciseRating rating: findAll()) {
            if (Objects.equals(rating.getUser(), user)) {
                ratings.add(rating);
            }
        }
        return ratings;
    }

    @Override
    public Set<ExerciseRating> findAllByExerciseId(Long id) {
        Set<ExerciseRating> ratings = new HashSet<>();

        for (ExerciseRating rating: findAll()) {
            if (Objects.equals(rating.getExercise().getId(), id)) {
                ratings.add(rating);
            }
        }
        return ratings;
    }

    private boolean isNotOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
