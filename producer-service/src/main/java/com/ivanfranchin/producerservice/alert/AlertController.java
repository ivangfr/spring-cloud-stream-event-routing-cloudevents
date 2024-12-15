package com.ivanfranchin.producerservice.alert;

import com.ivanfranchin.producerservice.alert.event.EarthquakeAlert;
import com.ivanfranchin.producerservice.alert.event.WeatherAlert;
import com.ivanfranchin.producerservice.alert.dto.CreateEarthquakeAlertRequest;
import com.ivanfranchin.producerservice.alert.dto.CreateWeatherAlertRequest;
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

    private final AlertEventEmitter alertEventEmitter;

    public AlertController(AlertEventEmitter alertEventEmitter) {
        this.alertEventEmitter = alertEventEmitter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/earthquake")
    public Mono<EarthquakeAlert> createEarthquakeAlert(@Valid @RequestBody CreateEarthquakeAlertRequest request) {
        EarthquakeAlert earthquakeAlert = new EarthquakeAlert(
                getId(), request.richterScale(), request.epicenterLat(), request.epicenterLon());
        alertEventEmitter.send(earthquakeAlert.id(), earthquakeAlert);
        return Mono.just(earthquakeAlert);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/weather")
    public Mono<WeatherAlert> createWeatherAlert(@Valid @RequestBody CreateWeatherAlertRequest request) {
        WeatherAlert weatherAlert = new WeatherAlert(getId(), request.message());
        alertEventEmitter.send(weatherAlert.id(), weatherAlert);
        return Mono.just(weatherAlert);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
