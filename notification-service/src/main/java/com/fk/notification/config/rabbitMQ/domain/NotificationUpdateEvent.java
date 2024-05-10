package com.fk.notification.config.rabbitMQ.domain;

import java.util.Arrays;
import java.util.LinkedHashMap;

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
      System.out.println(Arrays.toString(result.entrySet().toArray()));
      if(result.size() != 0){
        TemplateService.getAllByRules(topic, oldData, updatedData, result);
      }
      // @TODO: Notify users 
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
}
