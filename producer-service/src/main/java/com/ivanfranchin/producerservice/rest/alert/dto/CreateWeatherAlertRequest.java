package com.ivanfranchin.producerservice.rest.alert.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWeatherAlertRequest(@NotBlank String message) {
}
