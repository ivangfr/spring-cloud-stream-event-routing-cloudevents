package com.ivanfranchin.consumerservice.kafka.alert;

import com.ivanfranchin.consumerservice.ConsumerServiceApplication;
import com.ivanfranchin.consumerservice.kafka.alert.event.AlertEvent;
import com.ivanfranchin.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.ivanfranchin.consumerservice.kafka.alert.event.WeatherAlert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class AlertEventConsumerTest {

    @Test
    void testEarthquakeAlert(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEvent alertEvent = new EarthquakeAlert("id", 2.1, 1.0, -1.0);
            Message<AlertEvent> alertEventMessage = CloudEventMessageBuilder
                    .withData(alertEvent)
                    .setType("com.ivanfranchin.producerservice.kafka.alert.event.EarthquakeAlert")
                    .setHeader(PARTITION_KEY, "id")
                    .build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertEventMessage, DESTINATION_NAME);

            assertThat(output).contains("Received Earthquake alert!");
            assertThat(output).contains("PAYLOAD: EarthquakeAlert(id=id, richterScale=2.1, epicenterLat=1.0, epicenterLon=-1.0)");
        }
    }

    @Test
    void testWeatherAlert(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEvent alertEvent = new WeatherAlert("id", "message");
            Message<AlertEvent> alertEventMessage = CloudEventMessageBuilder
                    .withData(alertEvent)
                    .setType("com.ivanfranchin.producerservice.kafka.alert.event.WeatherAlert")
                    .setHeader(PARTITION_KEY, "id")
                    .build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertEventMessage, DESTINATION_NAME);

            assertThat(output).contains("Received Weather alert!");
            assertThat(output).contains("PAYLOAD: WeatherAlert(id=id, message=message)");
        }
    }

    private static final String DESTINATION_NAME = "alert.events";
    private static final String PARTITION_KEY = "partitionKey";
}