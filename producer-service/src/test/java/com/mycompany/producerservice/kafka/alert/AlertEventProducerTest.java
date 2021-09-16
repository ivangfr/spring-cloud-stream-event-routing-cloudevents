package com.mycompany.producerservice.kafka.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producerservice.ProducerServiceApplication;
import com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.producerservice.kafka.alert.event.WeatherAlert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class AlertEventProducerTest {

    @Test
    void testSendEarthquakeAlert() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEventProducer alertEventProducer = context.getBean(AlertEventProducer.class);
            EarthquakeAlert earthquakeAlert = EarthquakeAlert.of("id", 2.1, 1.0, -1.0);
            alertEventProducer.send(earthquakeAlert.getId(), earthquakeAlert);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
            MessageHeaders headers = outputMessage.getHeaders();
            EarthquakeAlert payload = deserialize(objectMapper, outputMessage.getPayload(), EarthquakeAlert.class);

            assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
            assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
            assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(EarthquakeAlert.class.getName());
            assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

            assertThat(payload).isNotNull();
            assertThat(payload.getId()).isEqualTo(earthquakeAlert.getId());
            assertThat(payload.getRichterScale()).isEqualTo(earthquakeAlert.getRichterScale());
            assertThat(payload.getEpicenterLat()).isEqualTo(earthquakeAlert.getEpicenterLat());
            assertThat(payload.getEpicenterLon()).isEqualTo(earthquakeAlert.getEpicenterLon());
        }
    }

    @Test
    void testSendWeatherAlert() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEventProducer alertEventProducer = context.getBean(AlertEventProducer.class);
            WeatherAlert weatherAlert = WeatherAlert.of("id", "message");
            alertEventProducer.send(weatherAlert.getId(), weatherAlert);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
            MessageHeaders headers = outputMessage.getHeaders();
            WeatherAlert payload = deserialize(objectMapper, outputMessage.getPayload(), WeatherAlert.class);

            assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
            assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
            assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(WeatherAlert.class.getName());
            assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

            assertThat(payload).isNotNull();
            assertThat(payload.getId()).isEqualTo(weatherAlert.getId());
            assertThat(payload.getMessage()).isEqualTo(weatherAlert.getMessage());
        }
    }

    private <T> T deserialize(ObjectMapper objectMapper, byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    private static final String BINDING_NAME = "alert.events";
    private static final String VERSION_1_0 = "1.0";
    private static final URI SOURCE_URI = URI.create("https://spring.io/");
    private static final String PARTITION_KEY = "partitionKey";
}