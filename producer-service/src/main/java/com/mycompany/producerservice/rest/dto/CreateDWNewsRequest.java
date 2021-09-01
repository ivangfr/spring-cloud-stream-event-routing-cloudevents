package com.mycompany.producerservice.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateDWNewsRequest {

    @NotBlank
    private String titel;
}
