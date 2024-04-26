package com.fk.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fk.notification.domain.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {

  @Query("{'userId': ?0}")
  public List<Notification> getNotificationsById(String userId);
}
