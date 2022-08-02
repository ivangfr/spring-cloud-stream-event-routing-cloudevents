package com.ivanfranchin.producerservice.kafka.news.event;

public record DWNewsCreated(String id, String titel) implements NewsEvent {
}
