package com.ivanfranchin.producerservice.kafka.news.event;

public record RAINewsCreated(String id, String titolo) implements NewsEvent {
}
