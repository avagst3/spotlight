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
    @PostMapping("/new")
    public ResponseEntity<TeamForUserDto> createTeam(@RequestBody TeamCreatedByUserDto dto) {
        TeamForUserDto team = userTeamService.userAddTeam(dto);
        return ResponseEntity.ok(team);
    }
    
    @Operation(summary = "Get all teams")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
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
    @GetMapping("/get/{id}")
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
    @PutMapping("/update/{id}")
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        boolean deleted = teamService.deleteTeam(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @Operation(summary = "Get user teams")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/my/{userId}")
    public ResponseEntity<List<TeamResponceDto>> getMyTeams(@PathVariable UUID userId) {
        List<TeamResponceDto> teams = userTeamService.getUserTeams(userId);
        return ResponseEntity.ok(teams);
    }
    @Operation(summary = "Update team profile picture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team updated successfully"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/update/{id}/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<TeamResponceDto> updateProfilePicture(@PathVariable UUID id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        TeamResponceDto team = teamService.updateTeamProfilePicture(id, file);
        return ResponseEntity.ok(team);
    }

    @Operation(summary = "Add user to team")
    @PostMapping("/{teamId}/users")
    public ResponseEntity<com.spotlight.back.spotlight.models.entities.UserTeam> addUser(@PathVariable UUID teamId, @RequestBody com.spotlight.back.spotlight.models.dtos.UserTeamDto dto) {
        // Ensure dto.teamId matches path variable or set it
        dto.setTeamId(teamId);
        return ResponseEntity.ok(userTeamService.createUserTeam(dto));
    }

    @Operation(summary = "Remove user from team")
    @DeleteMapping("/{teamId}/users/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable UUID teamId, @PathVariable UUID userId) {
        userTeamService.deleteUserTeam(teamId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update user role in team")
    @PutMapping("/{teamId}/users/{userId}")
    public ResponseEntity<Void> updateUserRole(@PathVariable UUID teamId, @PathVariable UUID userId, @RequestParam com.spotlight.back.spotlight.models.entities.UserTeam.UserRole role) {
        userTeamService.updateUserRole(teamId, userId, role);
        return ResponseEntity.ok().build();
    }
}