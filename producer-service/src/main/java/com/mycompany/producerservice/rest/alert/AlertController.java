package com.mycompany.producerservice.rest.alert;

import com.mycompany.producerservice.kafka.alert.AlertEventProducer;
import com.mycompany.producerservice.kafka.alert.AlertType;
import com.mycompany.producerservice.kafka.alert.event.AlertEvent;
import com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.producerservice.kafka.alert.event.WeatherAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertEventProducer alertEventProducer;

    @PostMapping("/earthquake")
    public Message<AlertEvent> createEarthquakeAlert(@Valid @RequestBody CreateEarthquakeAlertRequest request) {
        EarthquakeAlert earthquakeAlert = EarthquakeAlert.of(
                getId(), request.getRichterScale(), request.getEpicenterLat(), request.getEpicenterLon());
        return alertEventProducer.send(AlertType.EARTHQUAKE, earthquakeAlert);
    }

    @PostMapping("/weather")
    public Message<AlertEvent> createWeatherAlert(@Valid @RequestBody CreateWeatherAlertRequest request) {
        WeatherAlert weatherAlert = WeatherAlert.of(getId(), request.getMessage());
        return alertEventProducer.send(AlertType.WEATHER, weatherAlert);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
