package com.fk.notification.config.rabbitMQ.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fk.notification.config.rabbitMQ.RabbitConfiguration;
import com.fk.notification.config.rabbitMQ.domain.Event;
import com.fk.notification.service.NotificationService;

@Service
public class EventService {
  NotificationService notificationService;

  @Autowired
  EventService(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @RabbitListener(queues = RabbitConfiguration.queueName)
  private void receiveNotifications(Event event) {
    System.out.println(event);
    if (event != null) {
      event.handle(notificationService);
    }
  }
}
