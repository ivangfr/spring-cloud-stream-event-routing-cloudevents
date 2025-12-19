package com.ivanfranchin.producerservice.alert;

import com.ivanfranchin.producerservice.ProducerServiceApplication;
import com.ivanfranchin.producerservice.alert.event.EarthquakeAlert;
import com.ivanfranchin.producerservice.alert.event.WeatherAlert;
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
import tools.jackson.databind.ObjectMapper;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class AlertEventEmitterTest {

    @Test
    void testSendEarthquakeAlert() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEventEmitter alertEventEmitter = context.getBean(AlertEventEmitter.class);
            EarthquakeAlert earthquakeAlert = new EarthquakeAlert("id", 2.1, 1.0, -1.0);
            alertEventEmitter.send(earthquakeAlert.id(), earthquakeAlert);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);

            MessageHeaders headers = outputMessage.getHeaders();
            assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");
            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

            assertThat(CloudEventMessageUtils.getSource(outputMessage)).isEqualTo(SOURCE_URI);
            assertThat(CloudEventMessageUtils.getSpecVersion(outputMessage)).isEqualTo(VERSION_1_0);
            assertThat(CloudEventMessageUtils.getType(outputMessage)).isEqualTo(EarthquakeAlert.class.getName());
            assertThat(CloudEventMessageUtils.getId(outputMessage)).isNotNull();

            EarthquakeAlert payload = objectMapper.readValue(outputMessage.getPayload(), EarthquakeAlert.class);
            assertThat(payload).isNotNull();
            assertThat(payload.id()).isEqualTo(earthquakeAlert.id());
            assertThat(payload.richterScale()).isEqualTo(earthquakeAlert.richterScale());
            assertThat(payload.epicenterLat()).isEqualTo(earthquakeAlert.epicenterLat());
            assertThat(payload.epicenterLon()).isEqualTo(earthquakeAlert.epicenterLon());
        }
    }

    @Test
    void testSendWeatherAlert() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEventEmitter alertEventEmitter = context.getBean(AlertEventEmitter.class);
            WeatherAlert weatherAlert = new WeatherAlert("id", "message");
            alertEventEmitter.send(weatherAlert.id(), weatherAlert);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);

            MessageHeaders headers = outputMessage.getHeaders();
            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

            assertThat(CloudEventMessageUtils.getSource(outputMessage)).isEqualTo(SOURCE_URI);
            assertThat(CloudEventMessageUtils.getSpecVersion(outputMessage)).isEqualTo(VERSION_1_0);
            assertThat(CloudEventMessageUtils.getType(outputMessage)).isEqualTo(WeatherAlert.class.getName());
            assertThat(CloudEventMessageUtils.getId(outputMessage)).isNotNull();

            WeatherAlert payload = objectMapper.readValue(outputMessage.getPayload(), WeatherAlert.class);
            assertThat(payload).isNotNull();
            assertThat(payload.id()).isEqualTo(weatherAlert.id());
            assertThat(payload.message()).isEqualTo(weatherAlert.message());
        }
    }

    private static final String BINDING_NAME = "alert.events";
    private static final String VERSION_1_0 = "1.0";
    private static final URI SOURCE_URI = URI.create("https://spring.io/");
    private static final String PARTITION_KEY = "partitionKey";
}