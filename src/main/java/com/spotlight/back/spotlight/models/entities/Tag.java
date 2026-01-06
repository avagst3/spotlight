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
@Table(name = "tags")
@EqualsAndHashCode(callSuper = true)
public class Tag extends AuditDateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String label;
    
    @Column(nullable = false)
    private Double xCoordinate;
    
    @Column(nullable = false)
    private Double yCoordinate;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer fontSize = 12;
    
    @Column(nullable = false)
    @Builder.Default
    private String fontColor = "#000000";
    
    @Column(nullable = false)
    @Builder.Default
    private String backgroundColor = "#FFFFFF";

    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TagProject> projects;

}