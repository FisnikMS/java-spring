package com.fk.notification.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fk.notification.domain.Template;
import com.fk.notification.domain.mapper.TemplateCreationMapper;
import com.fk.notification.domain.records.CreateTemplateRecord;
import com.fk.notification.domain.records.UpdateTemplateRecord;
import com.fk.notification.exception.EntityNotFoundException;
import com.fk.notification.service.TemplateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/templates")
public class TemplateController {

  private TemplateService templateService;

  @Autowired
  public TemplateController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @GetMapping()
  public Page<Template> getAll(
      @RequestParam(required = false, name = "userId") Optional<String> userId,
      @RequestParam(required = false, name = "startDate") Optional<String> startDate,
      @RequestParam(required = false, name = "endDate") Optional<String> endDate,
      @RequestParam(defaultValue = "0", name = "page") Integer page,
      @RequestParam(defaultValue = "5", name = "size") Integer size) {
    return this.templateService.getAll(userId, startDate, endDate, page, size);
  }

  @DeleteMapping("/{userIds}")
  public List<Template> deleteTemplatesById(
      @PathVariable("userIds") List<String> userIds) {
    return this.templateService.deleteTemplatesById(userIds);
  }

  @PutMapping()
  public Template updateTemplate(@Valid @RequestBody UpdateTemplateRecord updateTemplateRecord)
      throws EntityNotFoundException {
    return this.templateService.updateTemplate(updateTemplateRecord);
  }

  @PostMapping()
  public Template createTemplate(@Valid @RequestBody() CreateTemplateRecord createTemplate) {
System.out.println(createTemplate.rules());
    return this.templateService.insert(new TemplateCreationMapper().apply(createTemplate));
  }

}
