package com.ivanfranchin.producerservice.kafka.news.event;

public record CNNNewsCreated(String id, String title) implements NewsEvent {
}
