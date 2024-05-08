package com.fk.notification.config.rabbitMQ.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fk.notification.config.rabbitMQ.domain.EventType;
import com.fk.notification.service.NotificationService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NotificationEvent.class, name = "Creation"),
        @JsonSubTypes.Type(value = NotificationEvent.class, name = "Deletion"),
        @JsonSubTypes.Type(value = NotificationUpdateEvent.class, name = "Update")
})
public abstract class Event implements Serializable {
  private EventType eventType;
  public abstract void handle(NotificationService notificationService);
}

