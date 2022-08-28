package com.kyratsous.runnersapp.services.favorites;

import com.kyratsous.runnersapp.model.User;

import java.util.Set;

public interface FavoriteService<T, ID>{

    Set<T> findAll();

    void save(T object);

    void deleteByObjectId(ID id, User user);

    Set<ID> findAllObjectIdsByUser(User user);
}
