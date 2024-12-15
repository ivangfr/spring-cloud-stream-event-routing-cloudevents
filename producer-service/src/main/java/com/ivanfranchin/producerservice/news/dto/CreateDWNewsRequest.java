package com.ivanfranchin.producerservice.news.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDWNewsRequest(@NotBlank String titel) {
}
