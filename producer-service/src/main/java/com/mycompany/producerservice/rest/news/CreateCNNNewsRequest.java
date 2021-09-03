package com.mycompany.producerservice.rest.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCNNNewsRequest {

    @NotBlank
    private String title;
}
