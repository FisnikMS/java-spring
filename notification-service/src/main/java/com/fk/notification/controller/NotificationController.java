package com.fk.notification.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fk.notification.NotificationApplication;
import com.fk.notification.domain.Notification;
import com.fk.notification.domain.mapper.NotificationCreationMapper;
import com.fk.notification.domain.records.CreateNotificationRecord;
import com.fk.notification.domain.records.UpdateNotificationRecord;
import com.fk.notification.exception.EntityNotFoundException;
import com.fk.notification.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

  private NotificationService notificationService;

  @Autowired
  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  // @TODO: extract userId automatically after implementing security layer and JWT
  @GetMapping(path = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter sse(@PathVariable("userId") String userId) {
    return notificationService.sse(userId);
  }

  @GetMapping("/{userId}")
  public List<Notification> getNotificationsById(@PathVariable("userId") String userId) {
    return this.notificationService.getNotificationsById(userId);
  }

  @GetMapping()
  public Page<Notification> getAll(
      @RequestParam(required = false, name = "userId") Optional<String> userId,
      @RequestParam(required = false, name = "read") Optional<Boolean> read,
      @RequestParam(required = false, name = "startDate") Optional<String> startDate,
      @RequestParam(required = false, name = "endDate") Optional<String> endDate,
      @RequestParam(defaultValue = "0", name = "page") Integer page,
      @RequestParam(defaultValue = "5", name = "size") Integer size) {
    return this.notificationService.getAll(userId, read, startDate, endDate, page, size);
  }

  @DeleteMapping("/{userIds}")
  public List<Notification> deleteNotificationsById(
      @PathVariable("userIds") List<String> userIds) {
    return this.notificationService.deleteNotificationsById(userIds);
  }

  @PutMapping()
  public Notification updateNotification(@Valid @RequestBody UpdateNotificationRecord updateNotificationRecord)
      throws EntityNotFoundException {
    return this.notificationService.updateNotification(updateNotificationRecord);
  }

  @PostMapping()
  public Notification createNotification(@Valid @RequestBody() CreateNotificationRecord createNotification) {
    return this.notificationService.insert(new NotificationCreationMapper().apply(createNotification));
  }

}
