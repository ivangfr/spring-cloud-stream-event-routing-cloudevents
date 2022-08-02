package com.ivanfranchin.consumerservice;

import com.ivanfranchin.consumerservice.kafka.news.event.CNNNewsCreated;
import com.ivanfranchin.consumerservice.kafka.news.event.DWNewsCreated;
import com.ivanfranchin.consumerservice.kafka.news.event.RAINewsCreated;
import com.ivanfranchin.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.ivanfranchin.consumerservice.kafka.alert.event.WeatherAlert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.support.management.micrometer.MicrometerMetricsCaptorConfiguration;
import org.springframework.nativex.hint.TypeHint;

import java.net.URI;

@TypeHint(
        types = {
                EarthquakeAlert.class, WeatherAlert.class,
                CNNNewsCreated.class, DWNewsCreated.class, RAINewsCreated.class,
                URI.class,
                MicrometerMetricsCaptorConfiguration.class
        }
)
@SpringBootApplication
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceApplication.class, args);
    }
}
