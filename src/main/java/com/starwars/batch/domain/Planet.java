package com.starwars.batch.domain;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by joaquinanton on 21/7/17.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet {

    private String name;

    @JsonProperty(value = "rotation_period")
    private String rotationPeriod;

    @JsonProperty(value = "orbital_period")
    private String orbitalPeriod;

    private String diameter;
    private String climate;
    private String gravity;
    private String terrain;

    @JsonProperty(value = "surface_water")
    private String surfaceWater;
    private String population;
}
