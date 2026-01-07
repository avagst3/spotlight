package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "teams")
@EqualsAndHashCode(callSuper = true)
public class Team extends AuditDateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserTeam> users;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TeamProject> projects;
    
    private String profilePictureUrl;

}