package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.TeamResponceDto;
import com.spotlight.back.spotlight.models.entities.Team;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamConverter implements Converter<Team, TeamResponceDto> {
    
    private final UserInTeamConverter userConverter;
    private final ProjectInTeamConverter projectConverter;

    @Override
    public TeamResponceDto convert(Team team) {
        if (team == null) {
            return null;
        }

        TeamResponceDto.TeamResponceDtoBuilder builder = TeamResponceDto.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription());
        
        if (team.getProfilePictureUrl() != null) {
            builder.profilePictureUrl(team.getProfilePictureUrl());
        }
        if (team.getUsers() != null) {
            builder.users(team.getUsers().stream().map(userConverter::convert).toList());
        }
        if (team.getProjects() != null) {
            builder.projects(team.getProjects().stream().map(projectConverter::convert).toList());
        }
        
        return builder.build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(Team.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(TeamResponceDto.class);
    }
}
