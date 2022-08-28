package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.RaceFavorite;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface RaceFavoriteRepository extends CrudRepository<RaceFavorite, Long> {
    boolean existsRaceFavoriteByRaceAndUser(@NotNull Race race, @NotNull User user);

    void deleteRaceFavoriteByRaceAndUser(@NotNull Race race, @NotNull User user);
}
