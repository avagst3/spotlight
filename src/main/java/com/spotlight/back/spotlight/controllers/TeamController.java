package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.TeamDto;
import com.spotlight.back.spotlight.models.entities.Team;
import com.spotlight.back.spotlight.services.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    
    private final TeamService teamService;
    
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
    
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody TeamDto dto) {
        Team team = teamService.createTeam(dto);
        return ResponseEntity.ok(team);
    }
    
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable UUID id) {
        Team team = teamService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable UUID id, @RequestBody TeamDto dto) {
        Team team = teamService.updateTeam(id, dto);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        boolean deleted = teamService.deleteTeam(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}