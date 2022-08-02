package com.ivanfranchin.producerservice.rest.news.dto;

import javax.validation.constraints.NotBlank;

public record CreateDWNewsRequest(@NotBlank String titel) {
}
