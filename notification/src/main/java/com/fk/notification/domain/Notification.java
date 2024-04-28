package com.fk.notification.domain;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
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
public class Notification {
  @Id
  private String id;
  private String userId;
  private String title;
  private String message;
  private Date createdAt;
  private Date updatedAt;
  private Boolean read;
}
