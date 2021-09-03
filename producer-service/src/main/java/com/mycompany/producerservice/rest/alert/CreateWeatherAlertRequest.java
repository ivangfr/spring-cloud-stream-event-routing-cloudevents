package com.mycompany.producerservice.rest.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWeatherAlertRequest {

    @NotBlank
    private String message;
}
