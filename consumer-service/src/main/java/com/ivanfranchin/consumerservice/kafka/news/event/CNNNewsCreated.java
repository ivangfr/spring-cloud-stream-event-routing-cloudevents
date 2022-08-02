package com.ivanfranchin.consumerservice.kafka.news.event;

public record CNNNewsCreated(String id, String title) implements NewsEvent {
}
