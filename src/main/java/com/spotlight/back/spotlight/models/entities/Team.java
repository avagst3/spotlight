package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "teams")
public class Team {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String profilePictureUrl;
    
    public Team() {}
    
    public Team(String name, String description, String profilePictureUrl) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.profilePictureUrl = profilePictureUrl;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}