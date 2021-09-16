package com.mycompany.consumerservice;

import com.mycompany.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.consumerservice.kafka.alert.event.WeatherAlert;
import com.mycompany.consumerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.RAINewsCreated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

import java.net.URI;

@TypeHint(
        types = {
                EarthquakeAlert.class, WeatherAlert.class,
                CNNNewsCreated.class, DWNewsCreated.class, RAINewsCreated.class,
                URI.class
        })
@SpringBootApplication
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceApplication.class, args);
    }
}
