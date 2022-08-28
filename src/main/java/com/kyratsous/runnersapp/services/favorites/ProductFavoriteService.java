package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ProductFavorite;
import com.kyratsous.runnersapp.repositories.favorites.ProductFavoriteRepository;
import com.kyratsous.runnersapp.services.ProductService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ProductFavoriteService implements FavoriteService<ProductFavorite, Long> {

    private final ProductFavoriteRepository repository;
    private final ProductService productService;

    public ProductFavoriteService(ProductFavoriteRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Override
    public Set<ProductFavorite> findAll() {
        Set<ProductFavorite> favorites = new HashSet<>();
        repository.findAll().forEach(favorites::add);
        return favorites;
    }

    @Override
    public void save(ProductFavorite favorite) {
        if (!repository.existsRaceFavoriteByProductAndUser(favorite.getProduct(), favorite.getUser()) &&
            favorite.getProduct() != null && favorite.getUser() != null)
            repository.save(favorite);
    }

    @Transactional
    @Override
    public void deleteByObjectId(Long product_id, User user) {
        repository.deleteRaceFavoriteByProductAndUser(productService.findById(product_id), user);
    }

    @Override
    public Set<Long> findAllObjectIdsByUser(User user) {
        Set<Long> productIds = new HashSet<>();

        for (ProductFavorite favorite: repository.findAll())
            if (Objects.equals(favorite.getUser(), user))
                productIds.add(favorite.getProduct().getId());

        return productIds;
    }
}
