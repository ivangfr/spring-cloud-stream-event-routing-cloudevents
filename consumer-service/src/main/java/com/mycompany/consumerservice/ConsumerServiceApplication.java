package com.mycompany.consumerservice;

import com.mycompany.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.consumerservice.kafka.alert.event.WeatherAlert;
import com.mycompany.consumerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.RAINewsCreated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.support.management.micrometer.MicrometerMetricsCaptorConfiguration;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;

import java.net.URI;

@NativeHint(
        types = @TypeHint(
                types = {
                        EarthquakeAlert.class, WeatherAlert.class,
                        CNNNewsCreated.class, DWNewsCreated.class, RAINewsCreated.class,
                        URI.class,
                        MicrometerMetricsCaptorConfiguration.class
                }
        )
)
@SpringBootApplication
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceApplication.class, args);
    }
}
