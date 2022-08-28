package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ExerciseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExerciseService implements CrudService<Exercise, Long> {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Set<Exercise> findAll() {
        Set<Exercise> exercises = new HashSet<>();
        exerciseRepository.findAll().forEach(exercises::add);
        return exercises;
    }

    @Override
    public Exercise findById(Long id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    @Override
    public Exercise save(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteById(Long id) {
        if (isNotCurrentUser(findById(id).getCoach()))
            return;

        exerciseRepository.deleteById(id);
    }

    @Override
    public void update(Exercise exercise) {
        Exercise currentExercise = findById(exercise.getId());

        if (isNotCurrentUser(currentExercise.getCoach()))
            return;

        currentExercise.setTitle(exercise.getTitle());
        currentExercise.setBody(exercise.getBody());
        currentExercise.setExperience(exercise.getExperience());
        currentExercise.setDistanceOptions(exercise.getDistanceOptions());
        currentExercise.setFieldOptions(exercise.getFieldOptions());

        save(currentExercise);
    }

    @Override
    public Set<Exercise> findAllByUser(User user) {
        return (user != null)? exerciseRepository.findAllByCoach(user): new HashSet<>();
    }

    public Set<Exercise> findAllByFilters(Map<String, String> filters) {
        Set<Exercise> results = findAll();

        if (filters.get("distance") != null) {
            Set<String> distanceOptions = new HashSet<>(Arrays.asList(filters.get("distance").split(",")));
            results.retainAll(exerciseRepository.findAllByDistanceOptionsIsIn(distanceOptions));
        }

        if (filters.get("field") != null) {
            Set<String> fieldOptions = new HashSet<>(Arrays.asList(filters.get("field").split(",")));
            results.retainAll(exerciseRepository.findAllByFieldOptionsIsIn(fieldOptions));
        }

        if (filters.get("experience") != null) {
            Set<String> experiences = new HashSet<>(Arrays.asList(filters.get("experience").split(",")));
            results.retainAll(exerciseRepository.findAllByExperienceIsIn(experiences));
        }

        return results;
    }

    private boolean isNotCurrentUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
