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
public class TagCreationDto {
    @NotNull
    public UUID projectId;
    @NotEmpty
    public String label;
    @NotNull
    public Double xCoordinate;
    @NotNull
    public Double yCoordinate;
    @NotNull
    public String fontColor;
    @NotNull
    public String backgroundColor;
}
