package com.ivanfranchin.consumerservice.kafka.news.event;

public record RAINewsCreated(String id, String titolo) implements NewsEvent {
}
