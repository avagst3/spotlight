package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "team_projects")
public class TeamProject {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    public TeamProject() {}
    
    public TeamProject(Team team, Project project) {
        this.id = UUID.randomUUID();
        this.team = team;
        this.project = project;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}