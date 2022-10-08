package com.kyratsous.runnersapp.repositories;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Set<Notification> findAllByReceiverOrderByDateDesc(User user);

    void deleteAllByReceiver(User user);

    boolean existsByTitle(String title);
}
