package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final Path videoStoragePath = Paths.get("uploads/videos");
    private final Path dataStoragePath = Paths.get("uploads/data");
    
    public ProjectService(ProjectRepository projectRepository) throws IOException {
        this.projectRepository = projectRepository;
        Files.createDirectories(videoStoragePath);
        Files.createDirectories(dataStoragePath);
    }
    
    public Project createProject(ProjectDto dto) {
        Project project = new Project(dto.name, dto.description);
        return projectRepository.save(project);
    }
    
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    
    public Project getProjectById(UUID id) {
        return projectRepository.findById(id).orElse(null);
    }
    
    public Project updateProject(UUID id, ProjectDto dto) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) return null;
        
        project.setName(dto.name);
        project.setDescription(dto.description);
        
        return projectRepository.save(project);
    }
    
    public Project updateProjectVideo(UUID projectId, MultipartFile videoFile) throws IOException {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return null;
        
        String fileName = projectId.toString() + "_" + System.currentTimeMillis() + 
                         getFileExtension(videoFile.getOriginalFilename());
        Path filePath = videoStoragePath.resolve(fileName);
        
        // Save file
        Files.copy(videoFile.getInputStream(), filePath);
        
        project.setSourceVideoPath(filePath.toString());
        return projectRepository.save(project);
    }
    
    public Project updateProjectData(UUID projectId, MultipartFile dataFile) throws IOException {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return null;
        
        String fileName = projectId.toString() + "_" + System.currentTimeMillis() + ".csv";
        Path filePath = dataStoragePath.resolve(fileName);
        
        Files.copy(dataFile.getInputStream(), filePath);
        
        project.setSourceDataPath(filePath.toString());
        return projectRepository.save(project);
    }
    
    public boolean deleteProject(UUID id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot);
    }
}