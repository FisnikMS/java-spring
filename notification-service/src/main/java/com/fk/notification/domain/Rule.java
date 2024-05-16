package com.fk.notification.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.index.TextIndexed;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Rule {
  @TextIndexed
  private String field;
  private Set<String> conditions = new HashSet<>();
  @TextIndexed
  private String exactValue;
}
