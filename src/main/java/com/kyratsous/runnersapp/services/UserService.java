package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.User;

public interface UserService extends CrudService<User, Long> {

    User getCurrentUser();

    void updatePassword(User user);
}
