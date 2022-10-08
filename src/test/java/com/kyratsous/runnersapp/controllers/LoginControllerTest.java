package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    LoginController loginController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {


        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/login")
                                .requestAttr("error", false)
                                .requestAttr("invalid-session", false)
                                .requestAttr("logout", false))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    void createCookies() throws Exception {
        Set<String> authorities = new HashSet<>();
        authorities.add("ATHLETE");
        User user = new User();
        user.setAuthorities(authorities);

        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/create-cookies").cookie(new Cookie("PREVIOUS-PAGE", "/")))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }
}