package com.mycompany.producerservice;

import com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.producerservice.kafka.alert.event.WeatherAlert;
import com.mycompany.producerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.producerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.producerservice.kafka.news.event.RAINewsCreated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

import java.util.LinkedHashMap;

@TypeHint(
        types = {
                EarthquakeAlert.class, WeatherAlert.class,
                CNNNewsCreated.class, DWNewsCreated.class, RAINewsCreated.class,
                LinkedHashMap.class
        })
@SpringBootApplication
public class ProducerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerServiceApplication.class, args);
    }

}
