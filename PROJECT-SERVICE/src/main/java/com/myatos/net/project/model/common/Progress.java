package com.myatos.net.project.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Progress {
    private int progress;
    private int total;
    private Integer percent;
}
