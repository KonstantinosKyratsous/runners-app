package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.User;

import java.util.Set;

public interface CrudService<T, ID> {

    Set<T> findAll();

    T findById(ID id);

    void save(T object);

    void delete(T object);

    void deleteById(ID id);

    void update(T object);

    Set<T> findAllByUserId(User user);
}