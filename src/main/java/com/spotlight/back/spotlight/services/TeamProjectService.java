package com.spotlight.back.spotlight.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spotlight.back.spotlight.models.converters.ProjectInTeamConverter;
import com.spotlight.back.spotlight.models.dtos.ProjectCreationDto;
import com.spotlight.back.spotlight.models.dtos.ProjectInTeamDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.models.entities.TeamProject;
import com.spotlight.back.spotlight.repositories.ProjectRepository;
import com.spotlight.back.spotlight.repositories.TeamProjectRepository;
import com.spotlight.back.spotlight.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamProjectService {
    
    private final TeamProjectRepository teamProjectRepository;
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final ProjectInTeamConverter projectInTeamConverter;

    @Transactional
    public TeamProject createTeamProject(TeamProject teamProject) {
        Team team = teamRepository.findById(teamProject.getTeam().getId()).orElseThrow(() -> new RuntimeException("Team not found"));
        Project project = projectRepository.findById(teamProject.getProject().getId()).orElseThrow(() -> new RuntimeException("Project not found"));
        teamProject.setTeam(team);
        teamProject.setProject(project);
        return teamProjectRepository.save(teamProject);
    }

    @Transactional
    public ProjectInTeamDto crateProjectInTeam(ProjectCreationDto projectCreationDto) {
        Team team = teamRepository.findById(projectCreationDto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
        Project project = projectRepository.save(Project.builder()
            .name(projectCreationDto.getName())
            .description(projectCreationDto.getDescription())
            .build());
        TeamProject teamProject = TeamProject.builder()
            .team(team)
            .project(project)
            .build();
        return projectInTeamConverter.convert(teamProjectRepository.save(teamProject));
    }

    @Transactional
    public void deleteTeamProject(TeamProject teamProject) {
        teamProjectRepository.delete(teamProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectInTeamDto> getProjectInTeam(UUID teamId) {
        return teamProjectRepository.findByTeamId(teamId).stream()
            .map(projectInTeamConverter::convert)
            .toList();
    }

}
