package com.ivanfranchin.consumerservice.news.event;

public record RAINewsCreated(String id, String titolo) implements NewsEvent {
}
