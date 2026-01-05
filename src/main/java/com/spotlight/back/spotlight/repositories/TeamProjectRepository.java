package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.models.entities.TeamProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamProjectRepository extends JpaRepository<TeamProject, UUID> {
    List<TeamProject> findByTeam(Team team);
    List<TeamProject> findByProject(Project project);
}