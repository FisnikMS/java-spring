package com.fk.application.domain;

import java.util.List;

import jakarta.annotation.Nonnull;

public record UpdateApplication(
    @Nonnull Long id,
    String version,
    String title,
    String description,
    List<RouteConfiguration> routeConfigurations) {

}
