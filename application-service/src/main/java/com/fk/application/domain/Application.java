package com.fk.application.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "application")
public class Application {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String createdBy;
  private String version;
  private String title;
  private String description;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "configuration", joinColumns = @JoinColumn(name = "application_id", nullable = false), uniqueConstraints = @UniqueConstraint(columnNames = {
      "application_id" }))
  private Set<RouteConfiguration> routeConfigurations = new HashSet<>();

}
