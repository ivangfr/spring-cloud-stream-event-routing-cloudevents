package com.mycompany.producerservice.rest.alert;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateWeatherAlertRequest {

    @NotBlank
    private String message;
}
