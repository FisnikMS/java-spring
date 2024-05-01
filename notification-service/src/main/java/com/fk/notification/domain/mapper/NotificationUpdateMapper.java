package com.fk.notification.domain.mapper;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.records.UpdateNotificationRecord;

import jakarta.validation.constraints.NotNull;

public class NotificationUpdateMapper implements Function<UpdateNotificationRecord, Notification> {

  Notification notification;

  public NotificationUpdateMapper(@NotNull Notification notification) {
    this.notification = notification;
  }

  public Notification apply(UpdateNotificationRecord t) {
    return new Notification()
        .builder()
        .id(t.id())
        .userId(t.userId() != null ? t.userId() : notification.getUserId())
        .message(t.message() != null ? t.message() : notification.getMessage())
        .title(t.title() != null ? t.title() : notification.getTitle())
        .read(t.read() != null ? t.read() : notification.getRead())
        .createdAt(notification.getCreatedAt())
        .updatedAt(new Date())
        .build();
  }

}
