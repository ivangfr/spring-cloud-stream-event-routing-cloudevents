package com.ivanfranchin.consumerservice.kafka.alert.event;

public record WeatherAlert(String id, String message) implements AlertEvent {
}
