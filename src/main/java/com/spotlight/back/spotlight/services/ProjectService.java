package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.converters.ProjectConverter;
import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.dtos.ProjectResponceDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final FileUploadService fileUploadService;
    
    @Transactional
    public ProjectResponceDto createProject(ProjectDto dto) {
        Project project = Project.builder()
                .name(dto.name)
                .description(dto.description)
                .build();
        return projectConverter.convert(projectRepository.save(project));
    }
    
    @Transactional(readOnly = true)
    public List<ProjectResponceDto> getAllProjects() {
        return  projectRepository.findAll().stream()
            .map(projectConverter::convert)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public ProjectResponceDto getProjectById(UUID id) {
        return projectConverter.convert(projectRepository.findById(id).get());
    }
    
    @Transactional
    public ProjectResponceDto updateProject(UUID id, ProjectDto dto) {
        Project project = projectRepository.findById(id).get();
        
        project.setName(dto.name);
        project.setDescription(dto.description);
        
        return projectConverter.convert(projectRepository.save(project));
    }
    
    @Transactional
    public ProjectResponceDto updateProjectVideo(UUID id, MultipartFile file) {
        Project project = projectRepository.findById(id).get();
        
        String path = fileUploadService.uploadFile(file,"project/raw");
        
        project.setSourceVideoPath(path);
        
        return projectConverter.convert(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponceDto updateProjectData(UUID id, MultipartFile file) {
        Project project = projectRepository.findById(id).get();
        
        String path = fileUploadService.uploadFile(file,"project/data");
        
        project.setSourceDataPath(path);
        
        return projectConverter.convert(projectRepository.save(project));
    }

    @Transactional
    public boolean deleteProject(UUID id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}