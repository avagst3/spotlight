package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}