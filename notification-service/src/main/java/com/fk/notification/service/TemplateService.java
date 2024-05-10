package com.fk.notification.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.print.Doc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.fk.notification.domain.Template;
import com.fk.notification.domain.records.UpdateTemplateRecord;
import com.fk.notification.domain.rules.Difference;
import com.fk.notification.domain.rules.Evaluation;
import com.fk.notification.domain.mapper.TemplateUpdateMapper;
import com.fk.notification.exception.EntityNotFoundException;
import com.fk.notification.repository.TemplateRepository;
import com.fk.notification.utils.Helper;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import org.springframework.data.mongodb.core.query.Query;

@Service
public class TemplateService {

  private TemplateRepository templateRepository;
  private MongoTemplate mongoTemplate;

  @Autowired
  public TemplateService(TemplateRepository templateRepository, MongoTemplate mongoTemplate,
      TaskScheduler taskScheduler) {
    this.templateRepository = templateRepository;
    this.mongoTemplate = mongoTemplate;
  }

  public Template insert(Template template) {
    return templateRepository.insert(template);
  }

  public Template updateTemplate(UpdateTemplateRecord updateTemplate) throws EntityNotFoundException {
    Optional<Template> template = templateRepository.findById(updateTemplate.id());

    if (template.isPresent()) {
      return templateRepository.save(new TemplateUpdateMapper(template.get()).apply(updateTemplate));
    }

    throw new EntityNotFoundException("Couldn't find template with id: " + updateTemplate.id());
  }

  public List<Template> deleteTemplatesById(List<String> ids) {
    List<Template> templates = templateRepository.findAllById(ids);
    templateRepository.deleteAllById(ids);
    return templates;
  }

  public Page<Template> getAll(
      Optional<String> userId,
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

    return PageableExecutionUtils.getPage(mongoTemplate.find(query, Template.class),
        pageable,
        () -> mongoTemplate.count(query.skip(0).limit(0), Template.class));
  }

  public HashSet<Template> getAllByRules(String topic, LinkedHashMap<String, Object> oldData,
      LinkedHashMap<String, Object> updatedData, LinkedHashMap<String, Difference> result) {

    List<Criteria> ruleCriterias = result.entrySet().stream().map(entry -> {
      Criteria orOperation = this.createOrAggregation(entry.getKey(), entry.getValue(),
          entry.getValue().getEvaluation());
      return orOperation;
    }).collect(Collectors.toList());

    Criteria topicMatch = Criteria.where("topic").is(topic);
    ruleCriterias.add(0, topicMatch);
    Criteria andOperation = new Criteria().andOperator(ruleCriterias);
    MatchOperation andMatch = Aggregation
        .match(andOperation);
    Aggregation agg = Aggregation.newAggregation(andMatch);
    return mongoTemplate.aggregate(agg, "templates", Template.class).getMappedResults();

  }

  private Criteria createOrAggregation(String key, Difference value, ArrayList<Evaluation> conditions) {
    return new Criteria().orOperator(
        createAndAggregation(key, value,
            (List<String>) conditions.stream().map(c -> c.toString()).collect(Collectors.toList())));
  }

  private List<Criteria> createAndAggregation(String key, List<String> conditions) {
    return createAndAggregation(key, null, conditions);
  }

  private List<Criteria> createAndAggregation(String key, Difference value, List<String> conditions) {
    List<Criteria> rules = new ArrayList<>();
    rules.add(new Criteria().andOperator(
        Criteria.where("rules.field").is(key),
        Criteria.where("rules.conditions").in(conditions)));
    if (value != null) {
      rules.add(new Criteria().andOperator(
          Criteria.where("exactValue").is(value.getNewValue()),
          Criteria.where("rules.conditions").in(conditions)));
    }
    return rules;
  }

}
