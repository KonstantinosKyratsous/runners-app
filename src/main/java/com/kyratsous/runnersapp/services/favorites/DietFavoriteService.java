package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.DietFavorite;
import com.kyratsous.runnersapp.repositories.favorites.DietFavoriteRepository;
import com.kyratsous.runnersapp.services.DietService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DietFavoriteService implements FavoriteService<DietFavorite, Long> {

    private final DietFavoriteRepository repository;
    private final DietService dietService;

    public DietFavoriteService(DietFavoriteRepository repository, DietService dietService) {
        this.repository = repository;
        this.dietService = dietService;
    }

    @Override
    public Set<DietFavorite> findAll() {
        Set<DietFavorite> favorites = new HashSet<>();
        repository.findAll().forEach(favorites::add);
        return favorites;
    }

    @Override
    public void save(DietFavorite favorite) {
        if (!repository.existsDietFavoriteByDietAndUser(favorite.getDiet(), favorite.getUser()) &&
            favorite.getDiet() != null && favorite.getUser() != null)
            repository.save(favorite);
    }

    @Transactional
    @Override
    public void deleteByObjectId(Long diet_id, User user) {
        repository.deleteDietFavoriteByDietAndUser(dietService.findById(diet_id), user);
    }

    @Override
    public Set<Long> findAllObjectIdsByUser(User user) {
        Set<Long> dietsId = new HashSet<>();

        for (DietFavorite favorite: repository.findAll())
            if (Objects.equals(favorite.getUser(), user))
                dietsId.add(favorite.getDiet().getId());

        return dietsId;
    }
}
