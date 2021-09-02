package com.mycompany.producerservice.rest.alert;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
public class CreateEarthquakeAlertRequest {

    @DecimalMin(value = "1.0")
    private Double richterScale;

    @NotNull
    private Double epicenterLat;

    @NotNull
    private Double epicenterLon;
}
