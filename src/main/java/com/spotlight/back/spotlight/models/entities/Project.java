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
@Table(name = "projects")
@EqualsAndHashCode(callSuper = true)
public class Project extends AuditDateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description",columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(name = "source_video_path")
    private String sourceVideoPath;
    
    @Column(name = "source_data_path")
    private String sourceDataPath;
    
    @Column(name = "processed_video_path")
    private String processedVideoPath;
    
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TeamProject> teams;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TagProject> tags;

}