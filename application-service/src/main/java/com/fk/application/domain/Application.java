package com.fk.application.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fk.application.domain.installation.Installation;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Table(name = "applications")
public class Application {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String createdBy;
  private String version;
  private String title;
  private String description;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "configuration", joinColumns = @JoinColumn(name = "application_id", nullable = false))
  private List<RouteConfiguration> routeConfigurations = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "application", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Installation> installations = new HashSet<>();

}
