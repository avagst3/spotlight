package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.exceptions.NotFoundException;
import com.spotlight.back.spotlight.exceptions.errors.TeamError;
import com.spotlight.back.spotlight.models.converters.TeamConverter;
import com.spotlight.back.spotlight.models.dtos.TeamDto;
import com.spotlight.back.spotlight.models.dtos.TeamResponceDto;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    
    private final TeamRepository teamRepository;
    private final TeamConverter teamConverter;
    
    @Transactional
    public Team createTeam(TeamDto dto) {
        Team team = Team.builder()
                .name(dto.name)
                .description(dto.description)
                .build();
        return teamRepository.save(team);
    }
    
    @Transactional(readOnly = true)
    public List<TeamResponceDto> getAllTeams() {
        return teamRepository.findAll().stream().map(teamConverter::convert).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TeamResponceDto getTeamById(UUID id) {
        return teamConverter.convert(teamRepository.findById(id).orElseThrow(() -> new NotFoundException(TeamError.TEAM_NOT_FOUND, id)));
    }
    
    @Transactional
    public TeamResponceDto updateTeam(UUID id, TeamDto dto) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) return null;
        
        team.setName(dto.name);
        team.setDescription(dto.description);
        
        return teamConverter.convert(teamRepository.save(team));
    }
    
    private final com.spotlight.back.spotlight.services.FileUploadService fileUploadService;

    public TeamResponceDto updateTeamProfilePicture(UUID id, org.springframework.web.multipart.MultipartFile file) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException(TeamError.TEAM_NOT_FOUND, id));
        String filename = fileUploadService.uploadFile(file, "team");
        team.setProfilePictureUrl(filename);
        return teamConverter.convert(teamRepository.save(team));
    }

    public boolean deleteTeam(UUID id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }
}