package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String sourceVideoPath;
    private String sourceDataPath;
    private String processedVideoPath;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessingStatus status = ProcessingStatus.PENDING;
    
    public enum ProcessingStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }
    
    public Project() {}
    
    public Project(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSourceVideoPath() { return sourceVideoPath; }
    public void setSourceVideoPath(String sourceVideoPath) { this.sourceVideoPath = sourceVideoPath; }
    
    public String getSourceDataPath() { return sourceDataPath; }
    public void setSourceDataPath(String sourceDataPath) { this.sourceDataPath = sourceDataPath; }
    
    public String getProcessedVideoPath() { return processedVideoPath; }
    public void setProcessedVideoPath(String processedVideoPath) { this.processedVideoPath = processedVideoPath; }
    
    public ProcessingStatus getStatus() { return status; }
    public void setStatus(ProcessingStatus status) { this.status = status; }
}