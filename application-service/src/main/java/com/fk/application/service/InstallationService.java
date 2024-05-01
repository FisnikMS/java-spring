package com.fk.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fk.application.domain.Application;
import com.fk.application.domain.installation.Installation;
import com.fk.application.repository.InstallationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

@Service
public class InstallationService {

  private InstallationRepository installationRepository;

  private ApplicationService applicationService;

  @Autowired
  InstallationService(InstallationRepository installationRepository, ApplicationService applicationService) {
    this.installationRepository = installationRepository;
    this.applicationService = applicationService;
  }

  public List<Installation> getAll() {
    return this.installationRepository.findAll();
  }

  // @TODO: extract userId automatically after implementing security layer and JWT
  public Installation save(Installation installation, Long applicationId) {
    Optional<Application> application = this.applicationService.getById(applicationId);
    if (application.isPresent()) {
      installation.setApplication(application.get());
      return this.installationRepository.save(installation);
    }
    throw new EntityNotFoundException("Couldn't find application with id: " +
        application);

  }

  public ResponseEntity delete(long installationId) {
    if (this.installationRepository.findById(installationId).isEmpty()) {
      throw new NotFoundException("Installation with id: " + installationId + " doesn't exist");
    }
    this.installationRepository.deleteById(installationId);
    return ResponseEntity.ok().build();
  }

}
