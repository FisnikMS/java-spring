package com.fk.application.domain.mapper;

import java.util.function.Function;

import com.fk.application.domain.Application;
import com.fk.application.domain.UpdateApplication;

public class UpdateApplicationMapper implements Function<UpdateApplication, Application> {

  private Application application;

  public UpdateApplicationMapper(Application application) {
    this.application = application;
  }

  @Override
  public Application apply(UpdateApplication app) {
    return new Application()
        .builder()
        .id(application.getId())
        .createdBy(application.getCreatedBy())
        .title(app.title() != null ? app.title() : application.getTitle())
        .description(app.description() != null ? app.description() : application.getDescription())
        .version(app.version() != null ? app.version() : application.getVersion())
        .routeConfigurations(app.routeConfigurations() != null ? app.routeConfigurations() : application.getRouteConfigurations())
        .build();
  }
}
