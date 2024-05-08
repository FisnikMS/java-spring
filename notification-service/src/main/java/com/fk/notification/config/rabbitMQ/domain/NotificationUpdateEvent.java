package com.fk.notification.config.rabbitMQ.domain;

import java.util.Arrays;
import java.util.LinkedHashMap;

import com.fk.notification.domain.rules.ComparisonWrapper;
import com.fk.notification.domain.rules.Difference;
import com.fk.notification.service.NotificationService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class NotificationUpdateEvent extends Event {
  private LinkedHashMap<String, Object> oldData;
  private LinkedHashMap<String, Object> updatedData;

  @Override
  public void handle(NotificationService notificationService) {
    try {
      LinkedHashMap<String, Difference> result = new ComparisonWrapper(oldData).compare(updatedData);
      System.out.println(Arrays.toString(result.entrySet().toArray()));
      // @TODO: MongoDB aggregation to get the ids of the users that subscribed to the
      // evaluated changes.
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
}
