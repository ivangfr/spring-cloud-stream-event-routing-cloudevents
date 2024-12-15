package com.ivanfranchin.producerservice.alert.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWeatherAlertRequest(@NotBlank String message) {
}
