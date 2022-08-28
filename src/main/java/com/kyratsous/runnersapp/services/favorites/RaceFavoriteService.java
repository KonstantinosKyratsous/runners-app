package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
import com.kyratsous.runnersapp.repositories.favorites.RaceFavoriteRepository;
import com.kyratsous.runnersapp.services.RaceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class RaceFavoriteService implements FavoriteService<RaceFavorite, Long> {

    private final RaceFavoriteRepository repository;
    private final RaceService raceService;

    public RaceFavoriteService(RaceFavoriteRepository repository, RaceService raceService) {
        this.repository = repository;
        this.raceService = raceService;
    }

    @Override
    public Set<RaceFavorite> findAll() {
        Set<RaceFavorite> favorites = new HashSet<>();
        repository.findAll().forEach(favorites::add);
        return favorites;
    }

    @Override
    public void save(RaceFavorite favorite) {
        if (!repository.existsRaceFavoriteByRaceAndUser(favorite.getRace(), favorite.getUser()) &&
            favorite.getRace() != null && favorite.getUser() != null)
            repository.save(favorite);
    }

    @Transactional
    @Override
    public void deleteByObjectId(Long race_id, User user) {
        repository.deleteRaceFavoriteByRaceAndUser(raceService.findById(race_id), user);
    }

    @Override
    public Set<Long> findAllObjectIdsByUser(User user) {
        Set<Long> raceIds = new HashSet<>();

        for (RaceFavorite favorite: repository.findAll())
            if (Objects.equals(favorite.getUser(), user))
                raceIds.add(favorite.getRace().getId());

        return raceIds;
    }
}
