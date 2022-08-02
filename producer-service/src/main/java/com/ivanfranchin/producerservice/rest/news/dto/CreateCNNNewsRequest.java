package com.ivanfranchin.producerservice.rest.news.dto;

import javax.validation.constraints.NotBlank;

public record CreateCNNNewsRequest(@NotBlank String title) {
}
