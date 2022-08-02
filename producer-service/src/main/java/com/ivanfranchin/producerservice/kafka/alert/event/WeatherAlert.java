package com.ivanfranchin.producerservice.kafka.alert.event;

public record WeatherAlert(String id, String message) implements AlertEvent {
}
