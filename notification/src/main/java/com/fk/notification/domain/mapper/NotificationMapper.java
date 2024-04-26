package com.fk.notification.domain.mapper;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.records.CreateNotificationRecord;

@Service
public class NotificationMapper implements Function<CreateNotificationRecord, Notification> {

  @Override
  public Notification apply(CreateNotificationRecord t) {
    return new Notification()
        .builder()
        .userId(t.userId())
        .message(t.message())
        .title(t.title())
        .read(false)
        .timestamp(new Date())
        .build();
  }

}
