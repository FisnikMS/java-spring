package com.fk.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fk.application.domain.Application;
import com.fk.application.domain.UpdateApplication;
import com.fk.application.domain.mapper.UpdateApplicationMapper;
import com.fk.application.repository.ApplicationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

@Service
public class ApplicationService {

  private ApplicationRepository applicationRepository;

  @Autowired
  ApplicationService(ApplicationRepository repo) {
    this.applicationRepository = repo;
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
    if (this.applicationRepository.findById(appId).isEmpty()) {
      throw new NotFoundException("Application with id: " + appId + " doesn't exist");
    }
    this.applicationRepository.deleteById(appId);
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
