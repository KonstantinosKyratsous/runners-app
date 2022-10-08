package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    User receiver;

    Notification notification;

    Notification notification2;

    @BeforeEach
    void setUp() {
        receiver = new User("First", "Last", "username", "test@email.com", "test",
                Arrays.stream(new String[]{"COACH"}).collect(Collectors.toSet()), null, "Novice", null);
        userRepository.save(receiver);

        notification = new Notification("Test Title", "Test Body", new Date(), "/test-url", receiver);
        notificationRepository.save(notification);

        notification2 = new Notification("Test Title 2", "Test Body 2", new Date(), "/test-url-2", receiver);
        notificationRepository.save(notification2);
    }

    @Test
    void findAllByReceiverOrderByDateDesc() {
        Set<Notification> notifications = notificationRepository.findAllByReceiverOrderByDateDesc(receiver);

        List<Date> dates = new ArrayList<>();
        for (Notification not: notifications) {
            dates.add(not.getDate());
        }

        assertEquals(2, notifications.size());
        assertTrue(dates.get(0).after(dates.get(1)));
    }

    @Test
    void deleteAllByReceiver() {
        notificationRepository.deleteAllByReceiver(receiver);

        assertEquals(0, notificationRepository.findAllByReceiverOrderByDateDesc(receiver).size());
    }

    @Test
    void existsByTitle() {
        assertTrue(notificationRepository.existsByTitle("Test Title"));
    }
}