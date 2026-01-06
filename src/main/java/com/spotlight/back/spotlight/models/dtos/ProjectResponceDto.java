package com.spotlight.back.spotlight.models.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponceDto {
    @NotEmpty
    public UUID id;
    @NotEmpty
    public String name;
    @NotEmpty
    public String description;
    public String sourceVideoPath;
    public String sourceDataPath;
    public String processedVideoPath;
    public List<TagDto> tags;
}
