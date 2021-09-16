package com.mycompany.consumerservice.kafka.news;

import com.mycompany.consumerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.RAINewsCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class NewsEventConsumer {

    @Bean
    public Consumer<Message<CNNNewsCreated>> cnnNewsCreated() {
        return message -> log.info(
                LOG_TEMPLATE, "Received news created message from CNN!", message.getHeaders(), message.getPayload());
    }

    @Bean
    public Consumer<Message<DWNewsCreated>> dwNewsCreated() {
        return message -> log.info(
                LOG_TEMPLATE, "Erhaltene Nachrichten erstellte Nachricht von DW!", message.getHeaders(), message.getPayload());
    }

    @Bean
    public Consumer<Message<RAINewsCreated>> raiNewsCreated() {
        return message -> log.info(
                LOG_TEMPLATE, "Ricevuta notizia creata messaggio da RAI!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
