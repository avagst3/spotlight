package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.converters.ProjectConverter;
import com.spotlight.back.spotlight.models.dtos.ProcessRequest;
import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.dtos.ProjectResponceDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.repositories.ProjectRepository;
import com.spotlight.back.spotlight.models.dtos.PythonProcessRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService implements org.springframework.beans.factory.InitializingBean {
    
    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final FileUploadService fileUploadService;

    @Value("${FILE_STORAGE_PATH:./uploads}")
    private String basePath;

    @Value("${PYTHON_API_URL}")
    private String pythonApiUrl;

    private org.springframework.web.reactive.function.client.WebClient webClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.webClient = org.springframework.web.reactive.function.client.WebClient.builder()
                .baseUrl(pythonApiUrl)
                .build();
    }

    
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
        return projectRepository.findById(id)
            .map(projectConverter::convert)
            .orElse(null);
    }
    
    @Transactional
    public ProjectResponceDto updateProject(UUID id, ProjectDto dto) {
        return projectRepository.findById(id)
            .map(project -> {
                project.setName(dto.name);
                project.setDescription(dto.description);
                return projectConverter.convert(projectRepository.save(project));
            })
            .orElse(null);
    }
    
    @Transactional
    public ProjectResponceDto updateProjectVideo(UUID id, MultipartFile file) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return null;
        }
        
        String path = fileUploadService.uploadFile(file,"project/raw");
        
        project.setSourceVideoPath(path);
        
        try {
            String absolutePath = basePath + "/" + path;
            
            // Asynchronous call (Fire-and-forget)
            webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/video/raw")
                            .queryParam("video_name", absolutePath)
                            .build())
                    .retrieve()
                    .bodyToMono(java.util.Map.class)
                    .map(response -> (String) response.get("task_id"))
                    .subscribe(taskId -> {
                        log.info("Started raw conversion task: {}", taskId);
                        // Update project in a new transaction/context
                        // Note: We need to re-fetch to ensure we have the latest state and don't overwrite concurrent changes
                        projectRepository.findById(id).ifPresent(p -> {
                            p.setCurrentTaskId(taskId);
                            projectRepository.save(p);
                        });
                    }, error -> {
                        log.error("Failed to trigger video conversion async", error);
                    });

        } catch (Exception e) {
            log.error("Failed to trigger video conversion", e);
        }

        return projectConverter.convert(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponceDto updateProjectData(UUID id, MultipartFile file) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return null;
        }
        
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

    @Transactional
public String startProjectProcessing(UUID id, ProcessRequest options) {
    // 1. Fetch Project Files from DB
    Project project = projectRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Project not found"));
    
    if (project.getSourceVideoPath() == null || project.getSourceDataPath() == null) {
        throw new RuntimeException("Missing source files");
    }

    // Use original video path - OpenCV can read most formats directly
    // If conversion to MP4 completed, it will be at the same path with .mp4 extension
    String sourceVideoPath = project.getSourceVideoPath();
    
    // Check if MP4 version exists (from raw conversion), otherwise use original
    String mp4VideoPath = sourceVideoPath.replaceAll("\\.[^.]+$", ".mp4");
    String videoPath;
    
    // In Docker, we can't check file existence from Java, so try original path first
    // The Python endpoint will handle file validation
    if (sourceVideoPath.toLowerCase().endsWith(".mp4")) {
        videoPath = basePath + "/" + sourceVideoPath;
    } else {
        // Try the converted MP4 version first, Python will validate
        videoPath = basePath + "/" + mp4VideoPath;
    }
    
    String dataPath = basePath + "/" + project.getSourceDataPath();
    String outputVideoPath = basePath + "/" + "project/video/" + id + "_processed.mp4";
    
    log.info("Processing video: {}", videoPath);
    log.info("Data file: {}", dataPath);
    log.info("Output: {}", outputVideoPath);

    
    PythonProcessRequest pythonRequest = new PythonProcessRequest();
    

    pythonRequest.setVideoInput(videoPath);
    pythonRequest.setDataInput(dataPath);
    pythonRequest.setVideoOutput(outputVideoPath);

    if (options != null) {
        pythonRequest.setConfigColonnes(options.getConfigColonnes());
        
        pythonRequest.setDecalageTemps(
            options.getDecalageTemps() != null ? options.getDecalageTemps() : 0.0
        );
        pythonRequest.setFreqRefresh(
            options.getFreqRefresh() != null ? options.getFreqRefresh() : 0.1
        );
    }

    // 3. Call Python API
    try {
        String taskId = webClient.post()
                .uri("/video/process")
                .bodyValue(pythonRequest) // Sends the merged object
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .map(response -> (String) response.get("task_id"))
                .block();
        
        // 4. Update DB status
        project.setCurrentTaskId(taskId);
        project.setProcessedVideoPath("project/video/" + id + "_processed.mp4");
        projectRepository.save(project);
        
        return taskId;
    } catch (Exception e) {
        log.error("Failed to start processing", e);
        throw new RuntimeException("Failed to start processing: " + e.getMessage());
    }
}

    public Object getProjectTaskStatus(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        
        if (project.getCurrentTaskId() == null) {
            return java.util.Map.of("status", "none");
        }
        
        try {
            return webClient.get()
                    .uri("/video/status/" + project.getCurrentTaskId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            return java.util.Map.of("status", "error", "message", e.getMessage());
        }
    }

    
}