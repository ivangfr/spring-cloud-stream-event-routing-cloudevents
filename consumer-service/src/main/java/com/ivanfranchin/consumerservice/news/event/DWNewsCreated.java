package com.ivanfranchin.consumerservice.news.event;

public record DWNewsCreated(String id, String titel) implements NewsEvent {
}
