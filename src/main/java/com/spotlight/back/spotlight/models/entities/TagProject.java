package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tag_projects")
public class TagProject {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    public TagProject() {}
    
    public TagProject(Tag tag, Project project) {
        this.id = UUID.randomUUID();
        this.tag = tag;
        this.project = project;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}