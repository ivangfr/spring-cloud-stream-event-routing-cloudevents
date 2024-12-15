package com.ivanfranchin.consumerservice.news;

import com.ivanfranchin.consumerservice.news.event.CNNNewsCreated;
import com.ivanfranchin.consumerservice.news.event.DWNewsCreated;
import com.ivanfranchin.consumerservice.news.event.RAINewsCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class NewsEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NewsEventConsumer.class);

    @Bean
    Consumer<Message<CNNNewsCreated>> cnnNewsCreated() {
        return message -> log.info(
                LOG_TEMPLATE, "Received news created message from CNN!", message.getHeaders(), message.getPayload());
    }

    @Bean
    Consumer<Message<DWNewsCreated>> dwNewsCreated() {
        return message -> log.info(
                LOG_TEMPLATE, "Erhaltene Nachrichten erstellte Nachricht von DW!", message.getHeaders(), message.getPayload());
    }

    @Bean
    Consumer<Message<RAINewsCreated>> raiNewsCreated() {
        return message -> log.info(
                LOG_TEMPLATE, "Ricevuta notizia creata messaggio da RAI!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
