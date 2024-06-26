package com.fk.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fk.notification.domain.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
