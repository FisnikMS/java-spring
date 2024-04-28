package com.fk.notification.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.fk.notification.domain.Notification;
import com.fk.notification.domain.mapper.NotificationUpdateMapper;
import com.fk.notification.domain.records.UpdateNotificationRecord;
import com.fk.notification.repository.NotificationRepository;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  public Notification insert(Notification notification) {
    return notificationRepository.insert(notification);
  }

  public Notification updateNotification(UpdateNotificationRecord updateNotification) {
    Optional<Notification> notification = notificationRepository.findById(updateNotification.id());

    if (notification.isPresent()) {
      return notificationRepository.save(new NotificationUpdateMapper(notification.get()).apply(updateNotification));
    }

    System.out.println("Could'nt find notification with id: " + updateNotification.id());

    return new Notification();

  }

  public List<Notification> getNotificationsById(String userId) {
    return notificationRepository.getNotificationsById(userId);
  }

  public List<Notification> deleteNotificationsById(List<String> ids) {
    List<Notification> notifications = notificationRepository.findAllById(ids);
    notificationRepository.deleteAllById(ids);
    return notifications;
  }

  private DateTimeFormatter createDateTimeFormatter(long defaultHourOfDay, long defaultOffsetSeconds) {
    return new DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE)
        .optionalStart()
        .parseCaseInsensitive()
        .appendPattern("[ ]['T']")
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .optionalEnd()
        .appendPattern("[xx]")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, defaultHourOfDay)
        .parseDefaulting(ChronoField.OFFSET_SECONDS, defaultOffsetSeconds)
        .toFormatter();

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
    DateTimeFormatter formatter = createDateTimeFormatter(0, 0);

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
      DateTimeFormatter endFormatter = createDateTimeFormatter(23, 60);
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
