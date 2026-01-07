package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@Table(name = "tag_projects")
@EqualsAndHashCode(callSuper = true)
public class TagProject extends AuditDateEntity {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
}