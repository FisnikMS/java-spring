package com.fk.application.config.rabbitMQ.domain;

import java.io.Serializable;
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
public class NotificationUpdateEvent<T> extends Event{
  private EventType eventType;
  private T oldData; 
  private T updatedData; 
}
