package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        User user1 = new User();
        User user2 = new User();
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        Set<User> usersAll = userService.findAll();

        assertNotNull(usersAll);
        assertEquals(2, usersAll.size());

        verify(userRepository).findAll();
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        User user = userService.findById(1L);

        assertNotNull(user);
        verify(userRepository).findById(1L);
    }

    @Test
    void save() {
        when(userRepository.save(any())).thenReturn(new User());

        User user = userService.save(new User());

        assertNotNull(user);
        verify(userRepository).save(any());
    }

    @Test
    void deleteById() {
        userService.deleteById(1L);

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    void getCurrentUser() {
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.setContext(context);

        userService.getCurrentUser();

        verify(userRepository).findUserByUsername(anyString());
    }

    @Test
    void updatePassword() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updatePassword(user);

        verify(userRepository).save(any());
    }

    @Test
    void findByUsername() {
        userService.findByUsername("test");

        verify(userRepository).findUserByUsername(anyString());
    }
}