package com.fk.notification.domain;
import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "notifications")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Notification implements Serializable {
  @Id
  private String id;
  private String userId;
  private String title;
  private String message;
  private Boolean read;
  @CreatedDate
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;
  @Version
  private Integer version;
}
