package com.fk.notification.config.rabbitMQ.domain;

import java.util.List;

import com.fk.notification.domain.Notification;
import com.fk.notification.service.NotificationService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent extends Event {
  private List<String> userIds;
  private String title;
  private String message;

  @Override
  public void handle(NotificationService notificationService) {
    userIds.stream().forEach(userId -> {
      notificationService.insert(Notification
          .builder()
          .userId(userId)
          .title(title)
          .message(message)
          .read(false)
          .build());
    });
  }
}
