package com.ivanfranchin.producerservice.news;

import com.ivanfranchin.producerservice.news.event.NewsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class NewsEventEmitter {

    private static final Logger log = LoggerFactory.getLogger(NewsEventEmitter.class);

    private final StreamBridge streamBridge;

    public NewsEventEmitter(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Value("${spring.cloud.stream.bindings.news-out-0.destination}")
    private String newsKafkaTopic;

    public Message<NewsEvent> send(String key, NewsEvent newsEvent) {
        Message<NewsEvent> message = CloudEventMessageBuilder.withData(newsEvent)
                .setHeader("partitionKey", key)
                .build();

        streamBridge.send("news-out-0", message);
        log.info("Sent message '{}' to topic '{}'", message, newsKafkaTopic);
        return message;
    }
}
