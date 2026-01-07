package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.ProjectResponceDto;
import com.spotlight.back.spotlight.models.entities.Project;

@Component
public class ProjectConverter implements Converter<Project, ProjectResponceDto> {
    
    @Override
    public ProjectResponceDto convert(Project project) {
        if (project == null) return null;

        ProjectResponceDto.ProjectResponceDtoBuilder builder = ProjectResponceDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .sourceVideoPath(project.getSourceVideoPath())
                .sourceDataPath(project.getSourceDataPath())
                .processedVideoPath(project.getProcessedVideoPath());

        return builder.build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(Project.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(ProjectResponceDto.class);
    }

}
