package com.ivanfranchin.consumerservice.kafka.alert.event;

public record EarthquakeAlert(String id, Double richterScale, Double epicenterLat,
                              Double epicenterLon) implements AlertEvent {
}
