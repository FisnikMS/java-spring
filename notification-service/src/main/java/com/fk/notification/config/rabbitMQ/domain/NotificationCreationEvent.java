package com.fk.notification.config.rabbitMQ.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreationEvent implements Serializable{
  private List<String> userIds;
  private String title;
  private String message;
}