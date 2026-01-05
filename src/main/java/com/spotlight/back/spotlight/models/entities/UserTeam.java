package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_teams")
public class UserTeam {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.MEMBER;
    
    public enum UserRole {
        OWNER, ADMIN, MEMBER, VIEWER
    }
    
    public UserTeam() {}
    
    public UserTeam(User user, Team team, UserRole role) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.team = team;
        this.role = role;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}