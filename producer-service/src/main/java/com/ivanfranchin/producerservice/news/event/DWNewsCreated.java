package com.ivanfranchin.producerservice.news.event;

public record DWNewsCreated(String id, String titel) implements NewsEvent {
}
