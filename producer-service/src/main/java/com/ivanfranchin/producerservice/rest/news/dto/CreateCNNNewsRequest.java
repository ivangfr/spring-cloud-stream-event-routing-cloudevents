package com.ivanfranchin.producerservice.rest.news.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCNNNewsRequest(@NotBlank String title) {
}
