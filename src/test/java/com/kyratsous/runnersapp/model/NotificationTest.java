package com.kyratsous.runnersapp.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationTest {

    static Notification notification;

    @BeforeEach
    public void setUp() {
        notification = new Notification();
    }

    @Test
    public void getTitle() {
        String title = "Title";

        notification.setTitle(title);
        assertEquals(title, notification.getTitle());
    }

    @Test
    public void getBody() {
        String body = "body";

        notification.setBody(body);
        assertEquals(body, notification.getBody());
    }

    @Test
    public void getUrl() {
        String url = "/test_url";

        notification.setUrl(url);
        assertEquals(url, notification.getUrl());
    }

    @Test
    public void getDate() {
        Date date = new Date();

        notification.setDate(date);
        assertEquals(date, notification.getDate());
    }

    @Test
    public void getReceiver() {
        User receiver = new User();

        notification.setReceiver(receiver);
        assertEquals(receiver, notification.getReceiver());
    }
}