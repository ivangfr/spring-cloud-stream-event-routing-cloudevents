package com.mycompany.producerservice.rest.news;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateRAINewsRequest {

    @NotBlank
    private String titolo;
}
