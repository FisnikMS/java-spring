package com.fk.notification.domain.mapper;

import java.util.Date;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fk.notification.domain.Template;
import com.fk.notification.domain.Rule;
import com.fk.notification.domain.records.UpdateTemplateRecord;

import jakarta.validation.constraints.NotNull;

public class TemplateUpdateMapper implements Function<UpdateTemplateRecord, Template> {

  Template template;

  public TemplateUpdateMapper(@NotNull Template template) {
    this.template = template;
  }

  public Template apply(UpdateTemplateRecord t) {
    return Template
        .builder()
        .id(t.id())
        .userId(t.userId() != null ? t.userId() : template.getUserId())
        .message(t.message() != null ? t.message() : template.getMessage())
        .title(t.title() != null ? t.title() : template.getTitle())
        .rules(t.rules() != null ? (Set<Rule>) t.rules().stream().map(r -> {
          return Rule
              .builder()
              .field(r.field())
              .exactValue(r.exactValue())
              .conditions(r.conditions())
              .build();

        }).collect(Collectors.toSet()) : template.getRules())
        .build();
  }

}
