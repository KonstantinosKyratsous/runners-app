package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DietRepository extends CrudRepository<Diet, Long> {
    Set<Diet> findAllByNutritionist(User user);
}
