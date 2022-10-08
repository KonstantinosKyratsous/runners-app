package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.TrainingPlanFavorite;
import com.kyratsous.runnersapp.repositories.favorites.TrainingPlanFavoriteRepository;
import com.kyratsous.runnersapp.services.TrainingPlanService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class TrainingPlanFavoriteService implements FavoriteService<TrainingPlanFavorite, Long> {

    private final TrainingPlanFavoriteRepository repository;
    private final TrainingPlanService trainingPlanService;

    public TrainingPlanFavoriteService(TrainingPlanFavoriteRepository repository, TrainingPlanService trainingPlanService) {
        this.repository = repository;
        this.trainingPlanService = trainingPlanService;
    }

    @Override
    public Set<TrainingPlanFavorite> findAll() {
        Set<TrainingPlanFavorite> favorites = new HashSet<>();
        repository.findAll().forEach(favorites::add);
        return favorites;
    }

    @Override
    public void save(TrainingPlanFavorite favorite) {
        if (!repository.existsTrainingPlanFavoriteByTrainingPlanAndUser(favorite.getTrainingPlan(), favorite.getUser()) &&
            favorite.getTrainingPlan() != null && favorite.getUser() != null)
            repository.save(favorite);
    }

    @Transactional
    @Override
    public void deleteByObjectId(Long training_plan_id, User user) {
        repository.deleteTrainingPlanFavoriteByTrainingPlanAndUser(trainingPlanService.findById(training_plan_id), user);
    }

    @Override
    public Set<Long> findAllObjectIdsByUser(User user) {
        Set<Long> trainingPlanIds = new HashSet<>();

        for (TrainingPlanFavorite favorite: repository.findAll())
            if (Objects.equals(favorite.getUser(), user))
                trainingPlanIds.add(favorite.getTrainingPlan().getId());

        return trainingPlanIds;
    }
}
