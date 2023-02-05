package com.ivanfranchin.producerservice.rest.alert.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateEarthquakeAlertRequest(@DecimalMin(value = "1.0") Double richterScale,
                                           @NotNull Double epicenterLat,
                                           @NotNull Double epicenterLon) {
}
