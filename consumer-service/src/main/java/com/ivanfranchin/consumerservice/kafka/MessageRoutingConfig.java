package com.ivanfranchin.consumerservice.kafka;

import com.ivanfranchin.consumerservice.properties.AppConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class MessageRoutingConfig {

    private static final Logger log = LoggerFactory.getLogger(MessageRoutingConfig.class);

    private final AppConfigurationProperties appConfigurationProperties;

    public MessageRoutingConfig(AppConfigurationProperties appConfigurationProperties) {
        this.appConfigurationProperties = appConfigurationProperties;
    }

    @Bean
    MessageRoutingCallback messageRoutingCallback() {
        return new MessageRoutingCallback() {
            @Override
            public String routingResult(Message<?> message) {
                return appConfigurationProperties.getRoutingMap()
                        .getOrDefault(CloudEventMessageUtils.getType(message), "unknownEvent");
            }
        };
    }

    @Bean
    Consumer<Message<?>> unknownEvent() {
        return message -> log.warn(LOG_TEMPLATE, "Received unknown event!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
