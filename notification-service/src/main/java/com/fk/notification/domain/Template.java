package com.fk.notification.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "templates")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Template {
  @Id
  private String id;
  private String userId;
  private String title;
  private String message;
  private String topic;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "rule", joinColumns = @JoinColumn(name = "template_id", nullable = false))
  private Set<Rule> rules = new HashSet<>();

  @CreatedDate
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;
  @Version
  private Integer version;
}
