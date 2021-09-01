package com.mycompany.producerservice.kafka;

import com.mycompany.producerservice.event.NewsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsEventProducer {

    @Value("${spring.cloud.stream.bindings.news-out-0.destination}")
    private String newsKafkaTopic;

    private final StreamBridge streamBridge;

    public void send(Broadcaster broadcaster, NewsEvent newsEvent) {
        Message<NewsEvent> message = MessageBuilder.withPayload(newsEvent)
                .setHeader("partitionKey", broadcaster.getType())
                .setHeader("broadcaster", broadcaster.getType())
                .build();

        streamBridge.send("news-out-0", message);
        log.info("Sent message '{}' to topic '{}'", message, newsKafkaTopic);
    }
}
