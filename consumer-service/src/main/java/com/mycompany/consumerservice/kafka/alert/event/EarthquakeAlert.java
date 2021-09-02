package com.mycompany.consumerservice.kafka.alert.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EarthquakeAlert implements AlertEvent {
    private String id;
    private Double richterScale;
    private Double epicenterLat;
    private Double epicenterLon;
}
