package com.spotlight.back.spotlight.models.converters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.TeamForUserDto;
import com.spotlight.back.spotlight.models.dtos.UserResponceDto;
import com.spotlight.back.spotlight.models.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<User, UserResponceDto> {
    
    private final TeamsOfUserConverter teamConverter;
    
    @Override
    public UserResponceDto convert(User user) {
        if (user == null) return null;

        UserResponceDto.UserResponceDtoBuilder builder = UserResponceDto.builder()
                .id(user.getId())
                .name(user.getName())
                .profilePictureUrl(user.getProfilePictureUrl());

        if (user.getUserTeams() != null) {
            List<TeamForUserDto> teamDtos = user.getUserTeams().stream()
                .map(teamConverter::convert)
                .collect(Collectors.toList());
            
            builder.teams(teamDtos);
        }

        return builder.build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(User.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(UserResponceDto.class);
    }
}
