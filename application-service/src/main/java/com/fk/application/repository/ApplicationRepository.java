package com.fk.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fk.application.domain.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long>{}

