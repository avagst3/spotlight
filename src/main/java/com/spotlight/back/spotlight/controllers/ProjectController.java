package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.ProjectCreationDto;
import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.dtos.ProjectInTeamDto;
import com.spotlight.back.spotlight.models.dtos.ProjectResponceDto;
import com.spotlight.back.spotlight.services.ProjectService;
import com.spotlight.back.spotlight.services.TeamProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    private final TeamProjectService teamProjectService;
   
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN','MEMBER')")
    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project created successfully",content = @Content(schema = @Schema(implementation = ProjectInTeamDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/new")
    public ResponseEntity<ProjectInTeamDto> createProject(@RequestBody ProjectCreationDto dto) {
        ProjectInTeamDto projectDto = teamProjectService.crateProjectInTeam(dto);
        return ResponseEntity.ok(projectDto);
    }
    
    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projects retrieved successfully",content = @Content(schema = @Schema(implementation = ProjectResponceDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponceDto>> getAllProjects() {
        List<ProjectResponceDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
    
    @Operation(summary = "Get a project by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project retrieved successfully",content = @Content(schema = @Schema(implementation = ProjectResponceDto.class))),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponceDto> getProject(@PathVariable UUID id) {
        ProjectResponceDto projectDto = projectService.getProjectById(id);
        return projectDto != null ? ResponseEntity.ok(projectDto) : ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Update a project by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project updated successfully",content = @Content(schema = @Schema(implementation = ProjectResponceDto.class))),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','MEMBER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponceDto> updateProject(@PathVariable UUID id, @RequestBody ProjectDto dto) {
        ProjectResponceDto projectDto = projectService.updateProject(id, dto);
        return projectDto != null ? ResponseEntity.ok(projectDto) : ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','MEMBER')")
    @Operation(summary = "Upload a video for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Video uploaded successfully",content = @Content(schema = @Schema(implementation = ProjectResponceDto.class))),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/video")
    public ResponseEntity<ProjectResponceDto> uploadVideo(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        ProjectResponceDto projectDto = projectService.updateProjectVideo(id, file);
        return projectDto != null ? ResponseEntity.ok(projectDto) : ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','MEMBER')")
    @Operation(summary = "Upload a data for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data uploaded successfully",content = @Content(schema = @Schema(implementation = ProjectResponceDto.class))),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/data")
    public ResponseEntity<ProjectResponceDto> uploadData(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        ProjectResponceDto projectDto = projectService.updateProjectData(id, file);
        return projectDto != null ? ResponseEntity.ok(projectDto) : ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','MEMBER')")
    @Operation(summary = "Delete a project by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Project deleted successfully",content = @Content(schema = @Schema(implementation = ProjectResponceDto.class))),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        boolean deleted = projectService.deleteProject(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}