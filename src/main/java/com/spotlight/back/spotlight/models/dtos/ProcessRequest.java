package com.spotlight.back.spotlight.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;
import java.util.HashMap;

@Data
public class ProcessRequest {

    @JsonProperty("decalage_temps")
    private Double decalageTemps = 0.0;

    @JsonProperty("freq_refresh")
    private Double freqRefresh = 0.1;

    @JsonProperty("config_colonnes")
    private Map<String, ColumnConfig> configColonnes = new HashMap<>();
}