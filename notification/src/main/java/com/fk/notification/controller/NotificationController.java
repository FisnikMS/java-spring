package com.fk.notification.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.mapper.NotificationMapper;
import com.fk.notification.domain.records.CreateNotificationRecord;
import com.fk.notification.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

  @Autowired
  private NotificationService notificationService;

  @GetMapping("/{userId}")
  public List<Notification> getNotificationsById(@PathVariable("userId") String userId) {
    return this.notificationService.getNotificationsById(userId);
  }

  @GetMapping("")
  public List<Notification> getAll(
      @RequestParam(required = false, name = "userId") Optional<String> userId,
      @RequestParam(required = false, name = "title") Optional<String> title,
      @RequestParam(required = false, name = "message") Optional<String> message,
      @RequestParam(required = false, name = "read") Optional<Boolean> read,
      @RequestParam(required = false, name = "startDate") Optional<String> startDate,
      @RequestParam(required = false, name = "endDate") Optional<String> endDate) {
    System.out.println(startDate.get());
    return this.notificationService.search(userId, title, message, read, startDate, endDate);
  }

  @PostMapping()
  public Notification createNotification(@Valid @RequestBody() CreateNotificationRecord createNotification) {
    return this.notificationService.insert(new NotificationMapper().apply(createNotification));
  }

}