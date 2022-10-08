package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.TrainingPlanRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainingPlanService implements CrudService<TrainingPlan, Long> {

    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
    }

    @Override
    public Set<TrainingPlan> findAll() {
        Set<TrainingPlan> trainingPlans = new HashSet<>();
        trainingPlanRepository.findAll().forEach(trainingPlans::add);
        return trainingPlans;
    }

    @Override
    public TrainingPlan findById(Long id) {
        return trainingPlanRepository.findById(id).orElse(null);
    }

    @Override
    public TrainingPlan save(TrainingPlan trainingPlan) {
        return trainingPlanRepository.save(trainingPlan);
    }

    @Override
    public void deleteById(Long id) {
        if (isNotCurrentUser(findById(id).getCoach()))
            return;

        trainingPlanRepository.deleteById(id);
    }

    @Override
    public void update(TrainingPlan trainingPlan) {
        TrainingPlan currentTrainingPlan = findById(trainingPlan.getId());

        if (isNotCurrentUser(currentTrainingPlan.getCoach()))
            return;

        currentTrainingPlan.setTitle(trainingPlan.getTitle());
        currentTrainingPlan.setBody(trainingPlan.getBody());
        currentTrainingPlan.setExperience(trainingPlan.getExperience());
        currentTrainingPlan.setDistanceOptions(trainingPlan.getDistanceOptions());
        currentTrainingPlan.setFieldOptions(trainingPlan.getFieldOptions());

        save(currentTrainingPlan);
    }

    @Override
    public Set<TrainingPlan> findAllByUser(User user) {
        return (user != null)? trainingPlanRepository.findAllByCoach(user): new HashSet<>();
    }

    public Set<TrainingPlan> findAllByFilters(Map<String, String> filters) {
        Set<TrainingPlan> results = findAll();

        if (filters.get("distance") != null) {
            Set<String> distanceOptions = new HashSet<>(Arrays.asList(filters.get("distance").split(",")));
            results.retainAll(trainingPlanRepository.findAllByDistanceOptionsIsIn(distanceOptions));
        }

        if (filters.get("field") != null) {
            Set<String> fieldOptions = new HashSet<>(Arrays.asList(filters.get("field").split(",")));
            results.retainAll(trainingPlanRepository.findAllByFieldOptionsIsIn(fieldOptions));
        }

        if (filters.get("experience") != null) {
            Set<String> experiences = new HashSet<>(Arrays.asList(filters.get("experience").split(",")));
            results.retainAll(trainingPlanRepository.findAllByExperienceIsIn(experiences));
        }

        return results;
    }

    private boolean isNotCurrentUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
