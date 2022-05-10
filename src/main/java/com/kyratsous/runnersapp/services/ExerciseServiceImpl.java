package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Exercise;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ExerciseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
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
    public void save(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    @Override
    public void delete(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

    @Override
    public void deleteById(Long id) {
        if (!isOwnerOfExercise(findById(id).getCoach())) {
            System.out.println("You cannot delete this exercise. You are not the owner of this exercise post. ");
            return;
        }
        exerciseRepository.deleteById(id);
    }

    @Override
    public void update(Exercise exercise) {
        Exercise currentExercise = findById(exercise.getId());

        if (!isOwnerOfExercise(currentExercise.getCoach())) {
            System.out.println("You cannot update this exercise. You are not the owner of this exercise post. ");
            return;
        }

        currentExercise.setTitle(exercise.getTitle());
        currentExercise.setBody(exercise.getBody());

        save(currentExercise);
    }

    @Override
    public Set<Exercise> findAllByUserId(User user) {
        Set<Exercise> exercises = new HashSet<>();

        if (user != null) {
            for (Exercise exercise: findAll()) {
                if (Objects.equals(exercise.getCoach().getId(), user.getId())) {
                    exercises.add(exercise);
                }
            }
        }

        return exercises;
    }

    private boolean isOwnerOfExercise(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return Objects.equals(auth.getName(), user.getUsername());
    }
}
