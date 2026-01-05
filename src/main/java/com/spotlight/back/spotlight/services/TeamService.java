package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.dtos.TeamDto;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamService {
    
    private final TeamRepository teamRepository;
    
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
    
    public Team createTeam(TeamDto dto) {
        Team team = new Team(
            dto.name,
            dto.description,
            dto.profilePictureUrl
        );
        return teamRepository.save(team);
    }
    
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
    
    public Team getTeamById(UUID id) {
        return teamRepository.findById(id).orElse(null);
    }
    
    public Team updateTeam(UUID id, TeamDto dto) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) return null;
        
        team.setName(dto.name);
        team.setDescription(dto.description);
        team.setProfilePictureUrl(dto.profilePictureUrl);
        
        return teamRepository.save(team);
    }
    
    public boolean deleteTeam(UUID id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }
}