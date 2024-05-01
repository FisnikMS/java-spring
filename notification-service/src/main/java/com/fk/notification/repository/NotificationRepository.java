package com.fk.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fk.notification.domain.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

  @Query("{'userId': ?0}")
  public List<Notification> getNotificationsById(String userId);
}
