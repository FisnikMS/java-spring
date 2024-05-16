package com.fk.notification.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.mapper.NotificationUpdateMapper;
import com.fk.notification.domain.records.UpdateNotificationRecord;
import com.fk.notification.exception.EntityNotFoundException;
import com.fk.notification.repository.NotificationRepository;
import com.fk.notification.utils.Helper;

@Service
public class NotificationService {

  private NotificationRepository notificationRepository;
  private MongoTemplate mongoTemplate;
  private TaskScheduler taskScheduler;
  private ScheduledFuture<?> scheduledFuture;
  private HashMap<String, SseEmitter> emitters;

  // @Value("${data.healthDuration:#{null}}")
  // private Long healthDuration;
  // @Value("${data.sseTimeout:#{null}}")
  // private Long sseTimeout;

  @Autowired
  public NotificationService(NotificationRepository notificationRepository, MongoTemplate mongoTemplate,
      TaskScheduler taskScheduler) {
    this.notificationRepository = notificationRepository;
    this.mongoTemplate = mongoTemplate;
    this.taskScheduler = taskScheduler;
    this.toggleScheduledTask(false);
    emitters = new HashMap<>();
  }

  private void health() {
    emitters.forEach((k, e) -> {
      try {
        e.send(SseEmitter.event().name("healthcheck").data("I'm alive."));
      } catch (IOException ex) {
        removeEmitter(k);
      }
    });
  }

  private void removeEmitter(String key) {
    SseEmitter emitter = emitters.remove(key);
    emitter.complete();
    if (emitters.keySet().isEmpty()) {
      toggleScheduledTask(false);
    }
  }

  private void toggleScheduledTask(boolean enable) {
    if (enable && (scheduledFuture == null || scheduledFuture.isCancelled())) {
      scheduledFuture = taskScheduler.schedule(this::health,
          new PeriodicTrigger(Duration.ofMillis(1000 * 60 * 5)));
    } else if (!enable && (scheduledFuture != null && !scheduledFuture.isCancelled())) {
      scheduledFuture.cancel(true);
    }
  }

  public SseEmitter sse(String userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    emitters.put(userId, emitter);
    toggleScheduledTask(true);
   
    emitter.onTimeout(() -> {
      removeEmitter(userId);
    });
    emitter.onCompletion(() -> {
      removeEmitter(userId);
    });
    return emitter;
  }

  public Notification insert(Notification notification) {
    SseEmitter emitter = emitters.get(notification.getUserId());
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name("data").data(notification));
      } catch (IOException e) {
        // @TODO logging.
        System.out.println("Log..");
      }
    }
    return notificationRepository.insert(notification);
  }

  public Notification updateNotification(UpdateNotificationRecord updateNotification) throws EntityNotFoundException {
    Optional<Notification> notification = notificationRepository.findById(updateNotification.id());

    if (notification.isPresent()) {
      return notificationRepository.save(new NotificationUpdateMapper(notification.get()).apply(updateNotification));
    }

    throw new EntityNotFoundException("Couldn't find notification with id: " + updateNotification.id());
  }

  public List<Notification> deleteNotificationsById(List<String> ids) {
    List<Notification> notifications = notificationRepository.findAllById(ids);
    notificationRepository.deleteAllById(ids);
    return notifications;
  }

  public Page<Notification> getAll(
      Optional<String> userId,
      Optional<Boolean> read,
      Optional<String> startDate,
      Optional<String> endDate,
      Integer page,
      Integer size) {
    Pageable pageable = PageRequest.of(page, size);
    Query query = new Query().with(pageable);
    List<Criteria> criteria = new ArrayList<>();
    DateTimeFormatter formatter = Helper.createDateTimeFormatter(0, 0);

    userId.ifPresent(u -> {
      criteria.add(Criteria.where("userId").is(u));
    });

    read.ifPresent(r -> {
      criteria.add(Criteria.where("read").is(r));
    });

    startDate.ifPresent(sd -> {
      Instant start = OffsetDateTime.parse(sd, formatter).toInstant();
      Criteria startDateCriteria = Criteria.where("createdAt").gte(start);
      criteria.add(startDateCriteria);
    });

    endDate.ifPresent(ed -> {
      DateTimeFormatter endFormatter = Helper.createDateTimeFormatter(23, 60);
      Instant end = OffsetDateTime.parse(ed, endFormatter).toInstant();
      Criteria endDateCriteria = Criteria.where("createdAt").lte(end);
      criteria.add(endDateCriteria);
    });

    if (!criteria.isEmpty()) {
      query.addCriteria(new Criteria().andOperator(criteria));
    }

    return PageableExecutionUtils.getPage(mongoTemplate.find(query, Notification.class),
        pageable,
        () -> mongoTemplate.count(query.skip(0).limit(0), Notification.class));
  }

}
