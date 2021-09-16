package com.mycompany.producerservice.kafka.alert;

import com.mycompany.producerservice.kafka.alert.event.AlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlertEventProducer {

    @Value("${spring.cloud.stream.bindings.alert-out-0.destination}")
    private String alertKafkaTopic;

    private final StreamBridge streamBridge;

    public Message<AlertEvent> send(String key, AlertEvent alertEvent) {
        Message<AlertEvent> message = CloudEventMessageBuilder.withData(alertEvent)
                .setHeader("partitionKey", key)
                .build();

        streamBridge.send("alert-out-0", message);
        log.info("Sent message '{}' to topic '{}'", message, alertKafkaTopic);
        return message;
    }
}
