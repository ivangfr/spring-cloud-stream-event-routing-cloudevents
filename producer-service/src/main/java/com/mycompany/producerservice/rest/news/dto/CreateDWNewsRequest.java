package com.mycompany.producerservice.rest.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDWNewsRequest {

    @NotBlank
    private String titel;
}
