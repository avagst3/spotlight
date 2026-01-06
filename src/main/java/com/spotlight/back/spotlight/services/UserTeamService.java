package com.spotlight.back.spotlight.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.spotlight.back.spotlight.exceptions.NotFoundException;
import com.spotlight.back.spotlight.exceptions.errors.UserErrorCode;
import com.spotlight.back.spotlight.models.dtos.TeamCreatedByUserDto;
import com.spotlight.back.spotlight.models.dtos.TeamForUserDto;
import com.spotlight.back.spotlight.models.dtos.TeamResponceDto;
import com.spotlight.back.spotlight.models.dtos.UserInTeamDto;
import com.spotlight.back.spotlight.models.dtos.UserTeamDto;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.models.entities.User;
import com.spotlight.back.spotlight.models.entities.UserTeam;
import com.spotlight.back.spotlight.repositories.TeamRepository;
import com.spotlight.back.spotlight.repositories.UserRepository;
import com.spotlight.back.spotlight.repositories.UserTeamRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTeamService {
    
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public UserTeam createUserTeam(UserTeamDto dto) {
        if (userTeamRepository.findByUserIdAndTeamId(dto.getUserId(), dto.getTeamId()).isPresent()) {
            throw new RuntimeException("User already in team");
        }
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
        UserTeam userTeam = UserTeam.builder()
                .user(user)
                .team(team)
                .role(dto.getRole())
                .build();
        return userTeamRepository.save(userTeam);
    }

    @Transactional
    public TeamForUserDto userAddTeam(TeamCreatedByUserDto dto) {
        Team team = Team.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        teamRepository.save(team);
        UserTeam userTeam = UserTeam.builder()
                .user(userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND, dto.getUserId())))
                .team(team)
                .role(UserTeam.UserRole.OWNER)
                .build();
        userTeamRepository.save(userTeam);
        return TeamForUserDto.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .build();
    }

    @Transactional
    public void updateUserRole(UUID teamId, UUID userId, UserTeam.UserRole newRole) {
        UserTeam membership = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new RuntimeException("User is not part of this team"));

        membership.setRole(newRole);
        userTeamRepository.save(membership);
    }

    @Transactional
    public void deleteUserTeam(UUID teamId, UUID userId) {
        UserTeam membership = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new RuntimeException("User is not part of this team"));
        userTeamRepository.delete(membership);
    }
    
    @Transactional(readOnly = true)
    public List<UserTeam> getUserTeamsByUserId(UUID userId) {
        return userTeamRepository.findByUserId(userId);
    }
    

    @Transactional(readOnly = true)
    public List<TeamResponceDto> getUserTeams(UUID userId) {
        List<UserTeam> userTeams = userTeamRepository.findByUserId(userId);
        return userTeams.stream()
        .map(ut -> TeamResponceDto.builder()
            .id(ut.getTeam().getId())
            .name(ut.getTeam().getName())
            .description(ut.getTeam().getDescription())
            .profilePictureUrl(ut.getTeam().getProfilePictureUrl())
            .build()
        )
        .toList();
    }

    @Transactional(readOnly = true)
    public List<UserInTeamDto> getTeamUsers(UUID teamId) {
        List<UserTeam> userTeams = userTeamRepository.findByTeamId(teamId);
        return userTeams.stream()
        .map(ut -> UserInTeamDto.builder()
            .id(ut.getUser().getId())
            .name(ut.getUser().getName())
            .profilePictureUrl(ut.getUser().getProfilePictureUrl())
            .role(ut.getRole())
            .build()
        )
        .toList();
    }
}
