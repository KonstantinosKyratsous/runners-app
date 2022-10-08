package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Notification;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationService implements CrudService<Notification, Long> {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Notification> findAll() {
        Set<Notification> notifications = new HashSet<>();
        repository.findAll().forEach(notifications::add);
        return notifications;
    }

    @Override
    public Notification findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAllByReceiver(User user) {
        repository.deleteAllByReceiver(user);
    }

    @Override
    public void update(Notification notification) {
        Notification current = findById(notification.getId());

        if (current != null) {
            current.setTitle(notification.getTitle());
            current.setBody(notification.getBody());

            repository.save(current);
        }
    }

    public boolean existsByTitle(String title) {
        return repository.existsByTitle(title);
    }

    @Override
    public Set<Notification> findAllByUser(User user) {
        return repository.findAllByReceiverOrderByDateDesc(user);
    }
}
