package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.TeamForUserDto;
import com.spotlight.back.spotlight.models.entities.UserTeam;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamsOfUserConverter implements Converter<UserTeam, TeamForUserDto> {
    
    private final TeamProjectConverter teamProjectConverter;

    @Override
    public TeamForUserDto convert(UserTeam userTeam) {
        return TeamForUserDto.builder()
                .id(userTeam.getTeam().getId())
                .name(userTeam.getTeam().getName())
                .description(userTeam.getTeam().getDescription())
                .profilePictureUrl(userTeam.getTeam().getProfilePictureUrl())
                .role(userTeam.getRole())
                .projects(userTeam.getTeam().getProjects().stream().map(teamProjectConverter::convert).toList())
                .build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(UserTeam.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(TeamForUserDto.class);
    }
}
