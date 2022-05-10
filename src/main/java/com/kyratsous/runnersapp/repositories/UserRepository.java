package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
