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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import com.fk.notification.domain.Notification;
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

  public List<Notification> getNotificationsById(String userId) {
    return this.notificationRepository.getNotificationsById(userId);
  }

  public List<Notification> search(
      Optional<String> userId,
      Optional<String> title,
      Optional<String> message,
      Optional<Boolean> read,
      Optional<String> startDate,
      Optional<String> endDate) {
    Query query = new Query();
    List<Criteria> criteria = new ArrayList<>();

    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE)
        .optionalStart()
        .parseCaseInsensitive()
        .appendPattern("[ ]['T']")
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .optionalEnd()
        .appendPattern("[xx]")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
        .toFormatter();

    userId.ifPresent(u -> {
      criteria.add(Criteria.where("userId").is(u));
    });

    title.ifPresent(t -> {
      criteria.add(Criteria.where("title").is(TextCriteria.forDefaultLanguage().matchingAny(t)));
    });

    message.ifPresent(m -> {
      criteria.add(Criteria.where("message").is(TextCriteria.forDefaultLanguage().matchingAny(m)));
    });

    read.ifPresent(r -> {
      criteria.add(Criteria.where("read").is(r));
    });

    startDate.ifPresent(sd -> {
      try {
        Instant start = OffsetDateTime.parse(sd, formatter).toInstant();
        System.out.println(start);
        Criteria startDateCriteria = Criteria.where("timestamp").gte(start);
        criteria.add(startDateCriteria);
      } catch (Exception e) {
        System.out.println("catched error");
      }
    });
    endDate.ifPresent(ed -> {
      try {
        Instant end =  OffsetDateTime.parse(ed, formatter).toInstant();
        Criteria endDateCriteria = Criteria.where("timestamp").lte(end);
        criteria.add(endDateCriteria);
      } catch (Exception e) {
        System.out.println("catched error");
      }
    });

    if (!criteria.isEmpty()) {
      query.addCriteria(new Criteria().andOperator(criteria));
    }

    return mongoTemplate.find(query, Notification.class);

  }

}
