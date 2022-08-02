package com.ivanfranchin.producerservice.rest.alert.dto;

import javax.validation.constraints.NotBlank;

public record CreateWeatherAlertRequest(@NotBlank String message) {
}
