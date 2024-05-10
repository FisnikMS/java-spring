package com.fk.notification.domain.mapper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fk.notification.domain.Template;
import com.fk.notification.domain.Rule;
import com.fk.notification.domain.records.CreateTemplateRecord;
import com.fk.notification.domain.records.UpdateTemplateRecord;

import jakarta.validation.constraints.NotNull;

public class TemplateCreationMapper implements Function<CreateTemplateRecord, Template> {

  public Template apply(CreateTemplateRecord t) {
    return Template
        .builder()
        .userId(t.userId())
        .message(t.message())
        .title(t.title())
        .topic(t.topic())
        .rules(
            (Set<Rule>) t.rules().stream().map(r -> {
              return Rule
                  .builder()
                  .field(r.field())
                  .exactValue(r.exactValue())
                  .conditions(r.conditions())
                  .build();

            }).collect(Collectors.toSet()))
        .build();
  }

}
