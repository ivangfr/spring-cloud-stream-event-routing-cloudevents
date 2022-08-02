package com.ivanfranchin.consumerservice.kafka.news.event;

public record DWNewsCreated(String id, String titel) implements NewsEvent {
}
