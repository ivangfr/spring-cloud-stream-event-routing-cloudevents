package com.ivanfranchin.consumerservice.kafka;

import com.ivanfranchin.consumerservice.ConsumerServiceApplication;
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
class MessageRoutingConfigTest {

    @Test
    void testUnknownEvent(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            Message<String> alertEventMessage = CloudEventMessageBuilder
                    .withData("Unknown Event")
                    .setType("com.ivanfranchin.producerservice.kafka.UnknownEvent")
                    .setHeader(PARTITION_KEY, "id")
                    .build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertEventMessage, DESTINATION_NAME);

            assertThat(output).contains("Received unknown event!");
            assertThat(output).contains("Unknown Event");
        }
    }

    private static final String DESTINATION_NAME = "news.events";
    private static final String PARTITION_KEY = "partitionKey";
}