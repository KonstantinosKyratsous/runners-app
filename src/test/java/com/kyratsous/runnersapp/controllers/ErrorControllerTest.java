package com.kyratsous.runnersapp.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class ErrorControllerTest {

    @InjectMocks
    ErrorController errorController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(errorController).build();
    }

    @Test
    void handleErrorUnknown() throws Exception {

        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error"));
    }

    @Test
    void handleError404() throws Exception {

        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute("javax.servlet.error.status_code", 404);
                            return request;}))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error-404"));
    }

    @Test
    void handleError403() throws Exception {

        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute("javax.servlet.error.status_code", 403);
                            return request;}))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error-403"));
    }

    @Test
    void handleError500() throws Exception {

        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute("javax.servlet.error.status_code", 500);
                            return request;}))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error-500"));
    }
}