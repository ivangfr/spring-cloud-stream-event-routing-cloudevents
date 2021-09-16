package com.mycompany.producerservice.kafka.news;

import com.mycompany.producerservice.kafka.news.event.NewsEvent;
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
public class NewsEventProducer {

    @Value("${spring.cloud.stream.bindings.news-out-0.destination}")
    private String newsKafkaTopic;

    private final StreamBridge streamBridge;

    public Message<NewsEvent> send(String key, NewsEvent newsEvent) {
        Message<NewsEvent> message = CloudEventMessageBuilder.withData(newsEvent)
                .setHeader("partitionKey", key)
                .build();

        streamBridge.send("news-out-0", message);
        log.info("Sent message '{}' to topic '{}'", message, newsKafkaTopic);
        return message;
    }
}
