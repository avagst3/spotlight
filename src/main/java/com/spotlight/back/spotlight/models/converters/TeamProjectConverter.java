package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.ProjectInTeamDto;
import com.spotlight.back.spotlight.models.entities.TeamProject;

@Component
public class TeamProjectConverter implements Converter<TeamProject, ProjectInTeamDto> {
    
    @Override
    public ProjectInTeamDto convert(TeamProject teamProject) {
        return ProjectInTeamDto.builder()
                .id(teamProject.getProject().getId())
                .name(teamProject.getProject().getName())
                .description(teamProject.getProject().getDescription())
                .build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(TeamProject.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(ProjectInTeamDto.class);
    }
}
