package com.fk.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.fk.notification.domain.Template;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {}
