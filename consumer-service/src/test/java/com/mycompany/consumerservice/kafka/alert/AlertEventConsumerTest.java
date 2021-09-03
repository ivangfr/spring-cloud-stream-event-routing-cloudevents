package com.mycompany.consumerservice.kafka.alert;

import com.mycompany.consumerservice.ConsumerServiceApplication;
import com.mycompany.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.consumerservice.kafka.alert.event.WeatherAlert;
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
    void testEarthquake(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            Message<EarthquakeAlert> message = CloudEventMessageBuilder
                    .withData(new EarthquakeAlert("1", 2.1, 1.0, -1.0))
                    .setHeader(PARTITION_KEY, "earthquake")
                    .setHeader(TYPE, "earthquake")
                    .build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(message, DESTINATION_NAME);

            assertThat(output).contains("Received Earthquake alert!");
            assertThat(output).contains("PAYLOAD: EarthquakeAlert(id=1, richterScale=2.1, epicenterLat=1.0, epicenterLon=-1.0)");
        }
    }

    @Test
    void testWeather(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            Message<WeatherAlert> message = CloudEventMessageBuilder
                    .withData(new WeatherAlert("1", "message"))
                    .setHeader(PARTITION_KEY, "weather")
                    .setHeader(TYPE, "weather")
                    .build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(message, DESTINATION_NAME);

            assertThat(output).contains("Received Weather alert!");
            assertThat(output).contains("PAYLOAD: WeatherAlert(id=1, message=message)");
        }
    }

    private static final String DESTINATION_NAME = "alert.events";
    private static final String PARTITION_KEY = "partitionKey";
    private static final String TYPE = "type";
}