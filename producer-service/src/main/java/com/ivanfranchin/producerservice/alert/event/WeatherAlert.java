package com.ivanfranchin.producerservice.alert.event;

public record WeatherAlert(String id, String message) implements AlertEvent {
}
