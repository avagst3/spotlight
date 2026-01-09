package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.converters.ProjectConverter;
import com.spotlight.back.spotlight.models.dtos.ProcessRequest;
import com.spotlight.back.spotlight.models.dtos.ProjectDto;
import com.spotlight.back.spotlight.models.dtos.ProjectResponceDto;
import com.spotlight.back.spotlight.models.entities.Project;
import com.spotlight.back.spotlight.repositories.ProjectRepository;
import com.spotlight.back.spotlight.models.dtos.PythonProcessRequest;
import com.spotlight.back.spotlight.exceptions.NotFoundException;
import com.spotlight.back.spotlight.exceptions.errors.ProjectError;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService implements InitializingBean {
    
    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final FileUploadService fileUploadService;

    @Value("${FILE_STORAGE_PATH:./uploads}")
    private String basePath;

    @Value("${PYTHON_API_URL}")
    private String pythonApiUrl;

    private WebClient webClient;

    @Override
    public void afterPropertiesSet() {
        this.webClient = WebClient.builder()
                .baseUrl(pythonApiUrl)
                .build();
    }

    @Transactional
    public ProjectResponceDto createProject(ProjectDto dto) {
        Project project = Project.builder()
                .name(dto.name)
                .description(dto.description)
                .status("PENDING")
                .build();
        return projectConverter.convert(projectRepository.save(project));
    }
    
    @Transactional(readOnly = true)
    public List<ProjectResponceDto> getAllProjects() {
        return projectRepository.findAll().stream()
            .map(projectConverter::convert)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public ProjectResponceDto getProjectById(UUID id) {
        return projectRepository.findById(id)
            .map(projectConverter::convert)
            .orElseThrow(() -> new NotFoundException(ProjectError.PROJECT_NOT_FOUND));
    }
    
    @Transactional
    public ProjectResponceDto updateProject(UUID id, ProjectDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectError.PROJECT_NOT_FOUND));
        project.setName(dto.name);
        project.setDescription(dto.description);
        return projectConverter.convert(projectRepository.save(project));
    }
    
    @Transactional
    public ProjectResponceDto updateProjectVideo(UUID id, MultipartFile file) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectError.PROJECT_NOT_FOUND));
        
        String path = fileUploadService.uploadFile(file, "project/raw");
        project.setSourceVideoPath(path);
        
        if (!path.toLowerCase().endsWith(".mp4")) {
            try {
                String absolutePath = basePath + "/" + path;
                webClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/video/raw")
                                .queryParam("video_name", absolutePath)
                                .build())
                        .retrieve()
                        .bodyToMono(Map.class)
                        .subscribe(response -> {
                            String taskId = (String) response.get("task_id");
                            updateProjectTaskInNewTransaction(id, taskId, "CONVERTING");
                        }, error -> log.error("Erreur Python /video/raw", error));

                project.setStatus("CONVERTING");
            } catch (Exception e) {
                log.error("Échec appel conversion", e);
            }
        } else {
            project.setProcessedVideoPath(path);
            project.setStatus("READY");
        }
        return projectConverter.convert(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponceDto updateProjectData(UUID id, MultipartFile file) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectError.PROJECT_NOT_FOUND));
        
        String path = fileUploadService.uploadFile(file, "project/data");
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

    private void updateProjectTaskInNewTransaction(UUID id, String taskId, String status) {
        projectRepository.findById(id).ifPresent(p -> {
            p.setCurrentTaskId(taskId);
            p.setStatus(status);
            projectRepository.save(p);
        });
    }

    @Transactional
    public String startProjectProcessing(UUID id, ProcessRequest options) {
        Project project = projectRepository.findById(id).orElseThrow();
        
        String sourceVideoPath = project.getSourceVideoPath();
        String videoInput = basePath + "/" + (sourceVideoPath.endsWith(".mp4") ? sourceVideoPath : sourceVideoPath.replaceAll("\\.[^.]+$", ".mp4"));
        String dataInput = basePath + "/" + project.getSourceDataPath();
        String relativeOutputPath = "project/video/" + id + "_processed.mp4";
        
        PythonProcessRequest pythonRequest = new PythonProcessRequest();
        pythonRequest.setVideoInput(videoInput);
        pythonRequest.setDataInput(dataInput);
        pythonRequest.setVideoOutput(basePath + "/" + relativeOutputPath);
        
        // Correction de type pour la Map de configuration
        pythonRequest.setConfigColonnes(options.getConfigColonnes());
        
        pythonRequest.setDecalageTemps(options.getDecalageTemps() != null ? options.getDecalageTemps() : 0.0);
        pythonRequest.setFreqRefresh(options.getFreqRefresh() != null ? options.getFreqRefresh() : 0.1);

        try {
            Map response = webClient.post().uri("/video/process").bodyValue(pythonRequest).retrieve().bodyToMono(Map.class).block();
            String taskId = (String) response.get("task_id");
            project.setCurrentTaskId(taskId);
            project.setStatus("PROCESSING");
            project.setProcessedVideoPath(relativeOutputPath);
            projectRepository.save(project);
            return taskId;
        } catch (Exception e) {
            throw new RuntimeException("Erreur Python: " + e.getMessage());
        }
    }

    public Object getProjectTaskStatus(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow();
        if (project.getCurrentTaskId() == null) return Map.of("status", "none");
        
        try {
            Map<String, Object> pythonResponse = (Map<String, Object>) webClient.get()
                    .uri("/video/status/" + project.getCurrentTaskId())
                    .retrieve().bodyToMono(Map.class).block();

            if ("completed".equals(pythonResponse.get("status"))) {
                project.setStatus("COMPLETED");
                String absoluteOutput = (String) pythonResponse.get("output");
                if (absoluteOutput != null) {
                    project.setProcessedVideoPath(absoluteOutput.replace(basePath + "/", ""));
                }
                projectRepository.save(project);
            }
            return pythonResponse;
        } catch (Exception e) {
            return Map.of("status", "error");
        }
    }
}