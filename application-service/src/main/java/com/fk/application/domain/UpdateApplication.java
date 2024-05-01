package com.fk.application.domain;

import java.util.Set;

import jakarta.annotation.Nonnull;

public record UpdateApplication(
    @Nonnull Long id,
    String version,
    String title,
    String description,
    Set<RouteConfiguration> routeConfigurations) {

}
