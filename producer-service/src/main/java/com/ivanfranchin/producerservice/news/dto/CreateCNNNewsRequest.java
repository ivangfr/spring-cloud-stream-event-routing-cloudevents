package com.ivanfranchin.producerservice.news.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCNNNewsRequest(@NotBlank String title) {
}
