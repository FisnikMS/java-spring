package com.fk.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fk.application.domain.Application;
import com.fk.application.domain.UpdateApplication;
import com.fk.application.domain.installation.Installation;
import com.fk.application.service.ApplicationService;
import com.fk.application.service.InstallationService;

@RestController
@RequestMapping("/api")
public class ApplicationController {

  @Autowired
  private ApplicationService applicationService;

  @Autowired
  private InstallationService installationService;

  @GetMapping("applications")
  public List<Application> getAll() {
    return this.applicationService.getAll();
  }

  @PostMapping("applications")
  public Application save(@RequestBody Application application) {
    return this.applicationService.save(application);
  }

  @PutMapping("applications")
  public Application update(@RequestBody UpdateApplication application) {
    return this.applicationService.update(application);
  }

  @DeleteMapping("applications/{appId}")
  public ResponseEntity delete(@PathVariable("appId") long id) {
    return this.applicationService.delete(id);
  }

  // Installation
  @GetMapping("installations")
  public List<Installation> getAllInstallations() {
    return this.installationService.getAll();
  }

  @PostMapping("installations/{applicationId}/application")
  public Installation saveInstallation(@RequestBody Installation installation,
      @PathVariable("applicationId") Long applicationId) {
    return this.installationService.save(installation, applicationId);
  }

  @DeleteMapping("installations/{installationId}")
  public ResponseEntity deleteInstallation(@PathVariable("installationId") long id) {
    return this.installationService.delete(id);
  }

}
