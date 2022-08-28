package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.DietFavorite;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface DietFavoriteRepository extends CrudRepository<DietFavorite, Long> {

    boolean existsDietFavoriteByDietAndUser(Diet diet, @NotNull User user);

    void deleteDietFavoriteByDietAndUser(Diet diet, @NotNull User user);
}
