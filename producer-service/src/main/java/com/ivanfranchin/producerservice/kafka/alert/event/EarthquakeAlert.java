package com.ivanfranchin.producerservice.kafka.alert.event;

public record EarthquakeAlert(String id, Double richterScale, Double epicenterLat,
                              Double epicenterLon) implements AlertEvent {
}
