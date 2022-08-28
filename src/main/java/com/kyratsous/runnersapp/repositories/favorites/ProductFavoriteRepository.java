package com.kyratsous.runnersapp.repositories.favorites;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ProductFavorite;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface ProductFavoriteRepository extends CrudRepository<ProductFavorite, Long> {

    boolean existsRaceFavoriteByProductAndUser(@NotNull Product product, @NotNull User user);

    void deleteRaceFavoriteByProductAndUser(@NotNull Product product, @NotNull User user);
}
