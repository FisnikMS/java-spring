package com.fk.application.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Embeddable

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class RouteConfiguration {
  private String label;
  private String remoteAddress; 
  private String remotePath;
  private String remoteName;
  private String module;
  private Boolean displayOnSidebar;
}
