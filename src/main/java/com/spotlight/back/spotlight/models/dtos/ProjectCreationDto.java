package com.spotlight.back.spotlight.models.dtos;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectCreationDto {
    @NotEmpty
    private UUID teamId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
