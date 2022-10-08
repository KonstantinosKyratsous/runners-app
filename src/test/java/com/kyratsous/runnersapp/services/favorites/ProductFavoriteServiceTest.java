package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ProductFavorite;
import com.kyratsous.runnersapp.repositories.favorites.ProductFavoriteRepository;
import com.kyratsous.runnersapp.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductFavoriteServiceTest {

    @InjectMocks
    ProductFavoriteService service;

    @Mock
    ProductFavoriteRepository repository;

    @Mock
    ProductService productService;

    @Test
    void findAll() {
        ProductFavorite favorite1 = new ProductFavorite();
        ProductFavorite favorite2 = new ProductFavorite();
        Set<ProductFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(repository.findAll()).thenReturn(favorites);

        Set<ProductFavorite> favoritesAll = service.findAll();

        assertNotNull(favoritesAll);
        assertEquals(2, favoritesAll.size());

        verify(repository).findAll();
    }

    @Test
    void save() {
        User user = new User();
        Product product = new Product();
        ProductFavorite favorite = new ProductFavorite(product, user);

        when(repository.existsProductFavoriteByProductAndUser(any(), any())).thenReturn(false);

        service.save(favorite);
        verify(repository).save(any());
    }

    @Test
    void deleteByObjectId() {
        when(productService.findById(anyLong())).thenReturn(new Product());
        service.deleteByObjectId(1L, new User());

        verify(repository).deleteProductFavoriteByProductAndUser(any(), any());
    }

    @Test
    void findAllObjectIdsByUser() {
        ProductFavorite favorite1 = new ProductFavorite();
        ProductFavorite favorite2 = new ProductFavorite();
        Set<ProductFavorite> favorites = new HashSet<>();
        favorites.add(favorite1);
        favorites.add(favorite2);

        when(repository.findAll()).thenReturn(favorites);

        Set<Long> ids = service.findAllObjectIdsByUser(new User());

        assertNotNull(ids);
        assertEquals(0, ids.size());

        verify(repository).findAll();
    }
}