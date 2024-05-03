package com.fk.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fk.application.config.rabbitMQ.RabbitConfiguration;
import com.fk.application.config.rabbitMQ.domain.NotificationCreationEvent;
import com.fk.application.domain.Application;
import com.fk.application.domain.UpdateApplication;
import com.fk.application.domain.mapper.UpdateApplicationMapper;
import com.fk.application.repository.ApplicationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

@Service
public class ApplicationService {

  private ApplicationRepository applicationRepository;

  private AmqpTemplate rabbitTemplate;

  @Autowired
  ApplicationService(ApplicationRepository repo, AmqpTemplate rabbitTemplate) {
    this.applicationRepository = repo;
    this.rabbitTemplate = rabbitTemplate;
  }

  private void notifyUser(NotificationCreationEvent notificationCreationEvent) {
    rabbitTemplate.convertAndSend(RabbitConfiguration.topicExchangeName, RabbitConfiguration.routingKey,
        notificationCreationEvent);
  }

  public List<Application> getAll() {
    return this.applicationRepository.findAll();
  }

  public Optional<Application> getById(Long applicationId) {
    return this.applicationRepository.findById(applicationId);
  }

  // @TODO: extract userId automatically after implementing security layer and JWT
  public Application save(Application app) {
    return this.applicationRepository.save(app);
  }

  public ResponseEntity delete(long appId) {
    Optional<Application> application = this.applicationRepository.findById(appId);
    if (application.isEmpty()) {
      throw new NotFoundException("Application with id: " + appId + " doesn't exist");
    }

    this.applicationRepository.deleteById(appId);

    if (!application.get().getInstallations().isEmpty()) {
      this.notifyUser(NotificationCreationEvent
          .builder()
          .title("Application " + application.get().getTitle() + " has been deleted.")
          .message("You've received this message because you installed this application.")
          .userIds(application.get().getInstallations().stream().map(notification -> notification.getUserId())
              .collect(Collectors.toList()))
          .build()
      );
    }

    return ResponseEntity.ok().build();
  }

  public Application update(UpdateApplication application) {
    Optional<Application> appToUpdate = this.applicationRepository.findById(application.id());
    if (appToUpdate.isPresent()) {
      return applicationRepository.save(new UpdateApplicationMapper(appToUpdate.get()).apply(application));
    }
    throw new EntityNotFoundException("Couldn't find application with id: " + application.id());
  }

}
