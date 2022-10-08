package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    NotificationRepository notificationRepository;

    @Test
    void findAll() {
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        Set<Notification> notifications = new HashSet<>();
        notifications.add(notification1);
        notifications.add(notification2);

        when(notificationRepository.findAll()).thenReturn(notifications);

        Set<Notification> notificationsAll = notificationService.findAll();

        assertNotNull(notificationsAll);
        assertEquals(2, notificationsAll.size());

        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(new Notification()));

        Notification notification = notificationService.findById(1L);

        assertNotNull(notification);
        verify(notificationRepository).findById(anyLong());
    }

    @Test
    void save() {
        when(notificationRepository.save(any())).thenReturn(new Notification());

        Notification notification = notificationService.save(new Notification());

        assertNotNull(notification);
        verify(notificationRepository).save(any());
    }

    @Test
    void deleteById() {
        notificationRepository.deleteById(1L);

        verify(notificationRepository).deleteById(anyLong());
    }

    @Test
    void deleteAllByReceiver() {
        notificationService.deleteAllByReceiver(new User());

        verify(notificationRepository).deleteAllByReceiver(any());
    }

    @Test
    void existsByTitle() {
        notificationService.existsByTitle("test");

        verify(notificationRepository).existsByTitle(anyString());
    }

    @Test
    void findAllByUser() {
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        Set<Notification> notificationsByUser = new HashSet<>();
        notificationsByUser.add(notification1);
        notificationsByUser.add(notification2);

        when(notificationRepository.findAllByReceiverOrderByDateDesc(any())).thenReturn(notificationsByUser);

        Set<Notification> notifications = notificationService.findAllByUser(new User());

        assertEquals(2, notifications.size());
        verify(notificationRepository).findAllByReceiverOrderByDateDesc(any());
    }
}