package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.UserInTeamDto;
import com.spotlight.back.spotlight.models.entities.UserTeam;

@Component
public class UserInTeamConverter implements Converter<UserTeam, UserInTeamDto> {
    
    @Override
    public UserInTeamDto convert(UserTeam userTeam) {
        return UserInTeamDto.builder()
                .id(userTeam.getId())
                .name(userTeam.getUser().getName())
                .profilePictureUrl(userTeam.getUser().getProfilePictureUrl())
                .role(userTeam.getRole())
                .build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(UserTeam.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(UserInTeamDto.class);
    }
}
