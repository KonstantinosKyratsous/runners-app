package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.NotificationService;
import com.kyratsous.runnersapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    NotificationService notificationService;

    @Mock
    PasswordEncoder passwordEncoder;

    User user = new User();

    @InjectMocks
    UserController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showProfile() throws Exception {
        when(notificationService.findAllByUser(any())).thenReturn(new HashSet<>());
        when(userService.findByUsername(anyString())).thenReturn(new User());

        mockMvc.perform(get("/user/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/public-profile"));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/user/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-update"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void processUpdateUser() throws Exception {
        mockMvc.perform(post("/user/update"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    void updatePassword() throws Exception {
        mockMvc.perform(get("/user/change_password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/password-update"));
    }

    @Test
    void processUpdatePassword() throws Exception {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        user.setPassword("$2a$10$.VVRFO67LAOPG63Cr2eZ1OLyxvB.bbEnIyhrHSoesl9ZNrdhnS/lS");
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(post("/user/change_password")
                        .flashAttr("current_password", "test"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    void updatePasswordWithError() throws Exception {
        mockMvc.perform(get("/user/change_password/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/password-update"))
                .andExpect(model().attribute("updateError", true));
    }
}