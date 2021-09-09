package com.mycompany.producerservice.rest.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEarthquakeAlertRequest {

    @DecimalMin(value = "1.0")
    private Double richterScale;

    @NotNull
    private Double epicenterLat;

    @NotNull
    private Double epicenterLon;
}
