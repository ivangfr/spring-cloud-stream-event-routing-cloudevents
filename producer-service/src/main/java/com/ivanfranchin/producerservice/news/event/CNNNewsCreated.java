package com.ivanfranchin.producerservice.news.event;

public record CNNNewsCreated(String id, String title) implements NewsEvent {
}
