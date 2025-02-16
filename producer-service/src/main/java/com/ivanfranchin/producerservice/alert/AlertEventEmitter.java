package com.ivanfranchin.producerservice.alert;

import com.ivanfranchin.producerservice.alert.event.AlertEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class AlertEventEmitter {

    private static final Logger log = LoggerFactory.getLogger(AlertEventEmitter.class);

    private final StreamBridge streamBridge;

    public AlertEventEmitter(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Value("${spring.cloud.stream.bindings.alert-out-0.destination}")
    private String alertKafkaTopic;

    public Message<AlertEvent> send(String key, AlertEvent alertEvent) {
        Message<AlertEvent> message = CloudEventMessageBuilder.withData(alertEvent)
                .setHeader("partitionKey", key)
                .build();

        streamBridge.send("alert-out-0", message);
        log.info("Sent message '{}' to topic '{}'", message, alertKafkaTopic);
        return message;
    }
}
