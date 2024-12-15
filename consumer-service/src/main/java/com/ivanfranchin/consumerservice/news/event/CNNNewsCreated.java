package com.ivanfranchin.consumerservice.news.event;

public record CNNNewsCreated(String id, String title) implements NewsEvent {
}
