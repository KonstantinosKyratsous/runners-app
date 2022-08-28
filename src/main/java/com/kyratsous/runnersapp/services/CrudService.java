package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.User;

import java.util.Set;

public interface CrudService<T, ID> {

    Set<T> findAll();

    T findById(ID id);

    T save(T object);

    void deleteById(ID id);

    void update(T object);

    Set<T> findAllByUser(User user);
}