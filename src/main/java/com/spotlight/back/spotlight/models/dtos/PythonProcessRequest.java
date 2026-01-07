package com.spotlight.back.spotlight.models.dtos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PythonProcessRequest {
    @JsonProperty("video_input")
    private String videoInput;

    @JsonProperty("data_input")
    private String dataInput;

    @JsonProperty("video_output")
    private String videoOutput;

    @JsonProperty("config_colonnes")
    private Map<String, ColumnConfig> configColonnes;

    @JsonProperty("decalage_temps")
    private double decalageTemps;

    @JsonProperty("freq_refresh")
    private double freqRefresh;
}