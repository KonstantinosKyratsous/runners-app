package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements CrudService<User, Long> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<User> findAll() {
        Set<User> users = new HashSet<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user) {
        User currentUser = userRepository.findUserByUsername(user.getUsername());

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmail(user.getEmail());
        currentUser.setAuthorities(user.getAuthorities());
        currentUser.setDistancePrefs(user.getDistancePrefs());
        currentUser.setExperience(user.getExperience());
        currentUser.setFieldTypes(user.getFieldTypes());

        userRepository.save(currentUser);
    }

    @Override
    public Set<User> findAllByUser(User user) {
        return null;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByUsername(auth.getName());
    }

    public void updatePassword(User user) {
        User currentUser = findById(user.getId());
        if (currentUser == null)
            return;

        currentUser.setPassword(user.getPassword());
        userRepository.save(currentUser);
    }

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
