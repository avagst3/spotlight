package com.spotlight.back.spotlight.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
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
    private Double decalageTemps;

    @JsonProperty("freq_refresh")
    private Double freqRefresh;
}