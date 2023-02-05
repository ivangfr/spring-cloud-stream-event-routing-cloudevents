package com.ivanfranchin.producerservice.rest.news.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRAINewsRequest(@NotBlank String titolo) {
}
