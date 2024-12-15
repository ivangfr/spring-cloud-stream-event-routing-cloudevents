package com.ivanfranchin.producerservice.news.event;

public record RAINewsCreated(String id, String titolo) implements NewsEvent {
}
