package com.spotlight.back.spotlight.repositories;

import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.models.entities.TeamProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamProjectRepository extends JpaRepository<TeamProject, UUID> {
    List<TeamProject> findByTeam(Team team);
    List<TeamProject> findByProject(Project project);
    List<TeamProject> findByTeamIdAndProjectId(UUID teamId, UUID projectId);
    List<TeamProject> findByTeamId(UUID teamId);
    List<TeamProject> findByProjectId(UUID projectId);
}