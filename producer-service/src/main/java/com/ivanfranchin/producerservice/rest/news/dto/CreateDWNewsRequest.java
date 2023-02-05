package com.ivanfranchin.producerservice.rest.news.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDWNewsRequest(@NotBlank String titel) {
}
