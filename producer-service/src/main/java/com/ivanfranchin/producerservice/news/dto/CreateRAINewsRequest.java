package com.ivanfranchin.producerservice.news.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRAINewsRequest(@NotBlank String titolo) {
}
