package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
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
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user) {
        Optional<User> currentUser = userRepository.findById(user.getId());

        currentUser.get().setFirstName(user.getFirstName());
        currentUser.get().setLastName(user.getLastName());
        currentUser.get().setEmail(user.getEmail());

        userRepository.save(currentUser.get());
    }

    @Override
    public Set<User> findAllByUserId(User user) {
        return null;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<User> users = findAll();

        for(User user: users) {
            if (user.getUsername().contentEquals(auth.getName())) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void updatePassword(User user) {
        Optional<User> currentUser = userRepository.findById(user.getId());

        currentUser.get().setPassword(user.getPassword());

        userRepository.save(currentUser.get());
    }
}
