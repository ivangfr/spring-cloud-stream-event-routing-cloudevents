package com.ivanfranchin.producerservice.rest.news.dto;

import javax.validation.constraints.NotBlank;

public record CreateRAINewsRequest(@NotBlank String titolo) {
}
