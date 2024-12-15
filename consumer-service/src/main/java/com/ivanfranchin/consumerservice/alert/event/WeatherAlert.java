package com.ivanfranchin.consumerservice.alert.event;

public record WeatherAlert(String id, String message) implements AlertEvent {
}
