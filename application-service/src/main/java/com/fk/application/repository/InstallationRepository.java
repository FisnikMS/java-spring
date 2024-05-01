package com.fk.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fk.application.domain.installation.Installation;

public interface InstallationRepository extends JpaRepository<Installation, Long>{}

