package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.NotificationService;
import com.kyratsous.runnersapp.services.RaceService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.RaceFavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    NotificationService notificationService;
    @Mock
    UserService userService;
    @Mock
    RaceService raceService;
    @Mock
    RaceFavoriteService raceFavoriteService;

    @InjectMocks
    NotificationController controller;

    MockMvc mockMvc;

    Set<Notification> notifications;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getNotifications() throws Exception {
        when(notificationService.findAllByUser(any())).thenReturn(notifications);
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteNotification() throws Exception {
        User user = new User();
        Notification notification = new Notification();
        notification.setReceiver(user);

        when(notificationService.findById(anyLong())).thenReturn(notification);

        mockMvc.perform(get("/notifications/delete/1"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteAllNotifications() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/notifications/delete-all"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void sendNotificationForm() throws Exception {
        mockMvc.perform(get("/notifications/send/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("notifications/send"));
    }

    @Test
    void sendNotification() throws Exception {
        when(raceService.findById(anyLong())).thenReturn(new Race());
        when(raceFavoriteService.findAll()).thenReturn(new HashSet<>());

        mockMvc.perform(post("/notifications/send/1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-races"));
    }
}