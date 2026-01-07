package com.spotlight.back.spotlight.models.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
    @NotNull
    private UUID id;
    @NotEmpty
    public String name;
    @NotEmpty
    public String description;    
}