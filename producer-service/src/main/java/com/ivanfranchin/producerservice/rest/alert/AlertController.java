package com.ivanfranchin.producerservice.rest.alert;

import com.ivanfranchin.producerservice.kafka.alert.AlertEventProducer;
import com.ivanfranchin.producerservice.kafka.alert.event.EarthquakeAlert;
import com.ivanfranchin.producerservice.kafka.alert.event.WeatherAlert;
import com.ivanfranchin.producerservice.rest.alert.dto.CreateEarthquakeAlertRequest;
import com.ivanfranchin.producerservice.rest.alert.dto.CreateWeatherAlertRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertEventProducer alertEventProducer;

    public AlertController(AlertEventProducer alertEventProducer) {
        this.alertEventProducer = alertEventProducer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/earthquake")
    public Mono<EarthquakeAlert> createEarthquakeAlert(@Valid @RequestBody CreateEarthquakeAlertRequest request) {
        EarthquakeAlert earthquakeAlert = new EarthquakeAlert(
                getId(), request.richterScale(), request.epicenterLat(), request.epicenterLon());
        alertEventProducer.send(earthquakeAlert.id(), earthquakeAlert);
        return Mono.just(earthquakeAlert);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/weather")
    public Mono<WeatherAlert> createWeatherAlert(@Valid @RequestBody CreateWeatherAlertRequest request) {
        WeatherAlert weatherAlert = new WeatherAlert(getId(), request.message());
        alertEventProducer.send(weatherAlert.id(), weatherAlert);
        return Mono.just(weatherAlert);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
