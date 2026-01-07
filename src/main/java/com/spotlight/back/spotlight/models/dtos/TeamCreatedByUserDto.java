package com.spotlight.back.spotlight.models.dtos;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreatedByUserDto {
    @NotNull
    private UUID userId;
    @NotNull
    private String name;
    private String description;
}
