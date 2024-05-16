package com.fk.notification.config.rabbitMQ.domain;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.Template;
import com.fk.notification.domain.rules.ComparisonWrapper;
import com.fk.notification.domain.rules.Difference;
import com.fk.notification.service.NotificationService;
import com.fk.notification.service.TemplateService;

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
  private String topic;
  private LinkedHashMap<String, Object> oldData;
  private LinkedHashMap<String, Object> updatedData;

  @Override
  public void handle(NotificationService notificationService, TemplateService TemplateService) {
    try {
      LinkedHashMap<String, Difference> result = new ComparisonWrapper(oldData).compare(updatedData);
      if (result.size() != 0) {
        List<Template> templates = TemplateService.getAllByRules(topic, oldData, updatedData, result);
        List<Notification> notifications = templates.stream().map(template -> {
          return Notification
              .builder()
              .userId(template.getUserId())
              .title(template.getTitle())
              .message(template.getMessage())
              .read(false)
              .build();
        }).collect(Collectors.toList());
        notifications.forEach(notificationService::insert);
      }

    } catch (Exception e) {
      // TODO: handle exception
    }
  }
}
