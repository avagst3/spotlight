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
public class TagDto {
    @NotEmpty
    public UUID id;
    @NotEmpty
    public String label;
    @NotNull
    public Double xCoordinate;
    @NotNull
    public Double yCoordinate;
    public Integer fontSize;
    @NotNull
    public String fontColor;
    @NotNull
    public UUID projectId;
    public String backgroundColor;
}