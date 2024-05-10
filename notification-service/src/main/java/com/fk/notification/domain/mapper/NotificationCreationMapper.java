package com.fk.notification.domain.mapper;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.records.CreateNotificationRecord;
import com.fk.notification.domain.records.UpdateNotificationRecord;

@Service
public class NotificationCreationMapper implements Function<CreateNotificationRecord, Notification> {

  @Override
  public Notification apply(CreateNotificationRecord t) {
    return Notification
        .builder()
        .userId(t.userId())
        .message(t.message())
        .title(t.title())
        .read(Boolean.valueOf(Boolean.FALSE))
        .build();
  }
}
