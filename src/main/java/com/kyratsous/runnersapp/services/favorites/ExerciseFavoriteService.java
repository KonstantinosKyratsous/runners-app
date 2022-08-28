package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ExerciseFavorite;
import com.kyratsous.runnersapp.repositories.favorites.ExerciseFavoriteRepository;
import com.kyratsous.runnersapp.services.ExerciseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ExerciseFavoriteService implements FavoriteService<ExerciseFavorite, Long> {

    private final ExerciseFavoriteRepository repository;
    private final ExerciseService exerciseService;

    public ExerciseFavoriteService(ExerciseFavoriteRepository repository, ExerciseService exerciseService) {
        this.repository = repository;
        this.exerciseService = exerciseService;
    }

    @Override
    public Set<ExerciseFavorite> findAll() {
        Set<ExerciseFavorite> favorites = new HashSet<>();
        repository.findAll().forEach(favorites::add);
        return favorites;
    }

    @Override
    public void save(ExerciseFavorite favorite) {
        if (!repository.existsRaceFavoriteByExerciseAndUser(favorite.getExercise(), favorite.getUser()) &&
            favorite.getExercise() != null && favorite.getUser() != null)
            repository.save(favorite);
    }

    @Transactional
    @Override
    public void deleteByObjectId(Long exercise_id, User user) {
        repository.deleteRaceFavoriteByExerciseAndUser(exerciseService.findById(exercise_id), user);
    }

    @Override
    public Set<Long> findAllObjectIdsByUser(User user) {
        Set<Long> exerciseIds = new HashSet<>();

        for (ExerciseFavorite favorite: repository.findAll())
            if (Objects.equals(favorite.getUser(), user))
                exerciseIds.add(favorite.getExercise().getId());

        return exerciseIds;
    }
}
