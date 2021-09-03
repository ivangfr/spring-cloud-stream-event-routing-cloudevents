package com.mycompany.producerservice.kafka.alert;

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

import java.net.URI;
import java.nio.charset.StandardCharsets;

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
            alertEventProducer.send(AlertType.EARTHQUAKE, EarthquakeAlert.of("1", 2.1, 1.0, -1.0));

            OutputDestination outputDestination = context.getBean(OutputDestination.class);
            Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
            MessageHeaders headers = outputMessage.getHeaders();
            String payloadStr = new String(outputMessage.getPayload(), StandardCharsets.UTF_8);

            assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
            assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
            assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(EarthquakeAlert.class.getName());
            assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(headers.get(PARTITION_KEY)).isEqualTo("earthquake");
            assertThat(headers.get(TYPE)).isEqualTo("earthquake");
            assertThat(payloadStr).isEqualTo("{\"id\":\"1\",\"richterScale\":2.1,\"epicenterLat\":1.0,\"epicenterLon\":-1.0}");
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
            alertEventProducer.send(AlertType.WEATHER, WeatherAlert.of("1", "message"));

            OutputDestination outputDestination = context.getBean(OutputDestination.class);
            Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
            MessageHeaders headers = outputMessage.getHeaders();
            String payloadStr = new String(outputMessage.getPayload(), StandardCharsets.UTF_8);

            assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
            assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
            assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(WeatherAlert.class.getName());
            assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(headers.get(PARTITION_KEY)).isEqualTo("weather");
            assertThat(headers.get(TYPE)).isEqualTo("weather");
            assertThat(payloadStr).isEqualTo("{\"id\":\"1\",\"message\":\"message\"}");
        }
    }

    private static final String BINDING_NAME = "alert.events";
    private static final String VERSION_1_0 = "1.0";
    private static final URI SOURCE_URI = URI.create("https://spring.io/");
    private static final String PARTITION_KEY = "partitionKey";
    private static final String TYPE = "type";
}