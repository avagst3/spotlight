package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_teams",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "team_id"})
})
@EqualsAndHashCode(callSuper = true)
public class UserTeam extends AuditDateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole role = UserRole.MEMBER;
    
    public enum UserRole {
        OWNER, ADMIN, MEMBER,VIEWER
    }

}