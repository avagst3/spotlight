package com.spotlight.back.spotlight.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnConfig {
    
    private List<Integer> pos; 

    @JsonProperty("font_color_hex")
    private String fontColorHex;

    @JsonProperty("bg_color_hex")
    private String bgColorHex;

    @JsonProperty("font_scale")
    private Double fontScale;

    private Integer thickness;
}