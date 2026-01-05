package com.spotlight.back.spotlight.models.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tags")
public class Tag {
    
    @Id
    private UUID id = UUID.randomUUID();
    
    @Column(nullable = false)
    private String label;
    
    @Column(nullable = false)
    private Double xCoordinate;
    
    @Column(nullable = false)
    private Double yCoordinate;
    
    @Column(nullable = false)
    private Integer fontSize = 12;
    
    @Column(nullable = false)
    private String fontColor = "#000000";
    
    @Column(nullable = false)
    private String backgroundColor = "#FFFFFF";
    
    public Tag() {}
    
    public Tag(String label, Double xCoordinate, Double yCoordinate) {
        this.id = UUID.randomUUID();
        this.label = label;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public Double getXCoordinate() { return xCoordinate; }
    public void setXCoordinate(Double xCoordinate) { this.xCoordinate = xCoordinate; }
    
    public Double getYCoordinate() { return yCoordinate; }
    public void setYCoordinate(Double yCoordinate) { this.yCoordinate = yCoordinate; }
    
    public Integer getFontSize() { return fontSize; }
    public void setFontSize(Integer fontSize) { this.fontSize = fontSize; }
    
    public String getFontColor() { return fontColor; }
    public void setFontColor(String fontColor) { this.fontColor = fontColor; }
    
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
}