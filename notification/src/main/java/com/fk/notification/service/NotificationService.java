package com.fk.notification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fk.notification.domain.Notification;
import com.fk.notification.repository.NotificationRepository;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  public Notification insert(Notification notification) {
    return notificationRepository.insert(notification);
  }

  public List<Notification> getNotificationsById(String userId) {
    return this.notificationRepository.getNotificationsById(userId);
  }

}
