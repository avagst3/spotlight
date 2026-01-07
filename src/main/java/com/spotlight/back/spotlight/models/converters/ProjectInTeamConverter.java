package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.dtos.ProjectInTeamDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.models.entities.TeamProject;

@Component
public class ProjectInTeamConverter implements Converter<TeamProject,ProjectInTeamDto>{
    
    @Override
    public ProjectInTeamDto convert(TeamProject teamProject) {
        if (teamProject == null) return null;

        ProjectInTeamDto.ProjectInTeamDtoBuilder builder = ProjectInTeamDto.builder()
                .id(teamProject.getProject().getId())
                .name(teamProject.getProject().getName())
                .description(teamProject.getProject().getDescription());

        return builder.build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(Project.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(ProjectDto.class);
    }

}
