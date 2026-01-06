package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.TeamCreatedByUserDto;
import com.spotlight.back.spotlight.models.dtos.TeamDto;
import com.spotlight.back.spotlight.models.dtos.TeamForUserDto;
import com.spotlight.back.spotlight.models.dtos.TeamResponceDto;
import com.spotlight.back.spotlight.services.TeamService;
import com.spotlight.back.spotlight.services.UserTeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;
    private final UserTeamService userTeamService;
    
    
    
    @Operation(summary = "Create a new team")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TeamForUserDto> createTeam(@RequestBody TeamCreatedByUserDto dto) {
        TeamForUserDto team = userTeamService.userAddTeam(dto);
        return ResponseEntity.ok(team);
    }
    
    @Operation(summary = "Get all teams")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TeamResponceDto>> getAllTeams() {
        List<TeamResponceDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }
    
    @Operation(summary = "Get a team by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponceDto> getTeam(@PathVariable UUID id) {
        TeamResponceDto team = teamService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @Operation(summary = "Update a team by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team updated successfully"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponceDto> updateTeam(@PathVariable UUID id, @RequestBody TeamDto dto) {
        TeamResponceDto team = teamService.updateTeam(id, dto);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Delete a team by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Team deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        boolean deleted = teamService.deleteTeam(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}