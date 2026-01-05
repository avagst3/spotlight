package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final ProjectService projectService;
    
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto dto) {
        Project project = projectService.createProject(dto);
        return ResponseEntity.ok(project);
    }
    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable UUID id) {
        Project project = projectService.getProjectById(id);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody ProjectDto dto) {
        Project project = projectService.updateProject(id, dto);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/video")
    public ResponseEntity<Project> uploadVideo(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            Project project = projectService.updateProjectVideo(id, file);
            return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{id}/data")
    public ResponseEntity<Project> uploadData(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            Project project = projectService.updateProjectData(id, file);
            return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        boolean deleted = projectService.deleteProject(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}