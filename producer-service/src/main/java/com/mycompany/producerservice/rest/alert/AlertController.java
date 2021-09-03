package com.mycompany.producerservice.rest.alert;

import com.mycompany.producerservice.kafka.alert.AlertEventProducer;
import com.mycompany.producerservice.kafka.alert.AlertType;
import com.mycompany.producerservice.kafka.alert.event.AlertEvent;
import com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.producerservice.kafka.alert.event.WeatherAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertEventProducer alertEventProducer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/earthquake")
    public Mono<Message<AlertEvent>> createEarthquakeAlert(@Valid @RequestBody CreateEarthquakeAlertRequest request) {
        EarthquakeAlert earthquakeAlert = EarthquakeAlert.of(
                getId(), request.getRichterScale(), request.getEpicenterLat(), request.getEpicenterLon());
        Message<AlertEvent> alertEventMessage = alertEventProducer.send(AlertType.EARTHQUAKE, earthquakeAlert);
        return Mono.just(alertEventMessage);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/weather")
    public Mono<Message<AlertEvent>> createWeatherAlert(@Valid @RequestBody CreateWeatherAlertRequest request) {
        WeatherAlert weatherAlert = WeatherAlert.of(getId(), request.getMessage());
        Message<AlertEvent> alertEventMessage = alertEventProducer.send(AlertType.WEATHER, weatherAlert);
        return Mono.just(alertEventMessage);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
