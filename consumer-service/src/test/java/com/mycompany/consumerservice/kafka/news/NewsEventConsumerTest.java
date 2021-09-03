package com.mycompany.consumerservice.kafka.news;

import com.mycompany.consumerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.consumerservice.kafka.news.event.NewsEvent;
import com.mycompany.consumerservice.kafka.news.event.RAINewsCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class NewsEventConsumerTest {

    @Autowired
    private InputDestination inputDestination;

    @Test
    void testCNN(CapturedOutput output) {
        NewsEvent newsEvent = new CNNNewsCreated("1", "title");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder
                .withData(newsEvent)
                .setHeader(PARTITION_KEY, "cnn")
                .setHeader(TYPE, "cnn")
                .build();

        inputDestination.send(newsEventMessage, DESTINATION_NAME);

        assertThat(output).contains("Received news created message from CNN!");
        assertThat(output).contains("PAYLOAD: CNNNewsCreated(id=1, title=title)");
    }

    @Test
    void testDW(CapturedOutput output) {
        NewsEvent newsEvent = new DWNewsCreated("1", "titel");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder
                .withData(newsEvent)
                .setHeader(PARTITION_KEY, "dw")
                .setHeader(TYPE, "dw")
                .build();

        inputDestination.send(newsEventMessage, DESTINATION_NAME);

        assertThat(output).contains("Erhaltene Nachrichten erstellte Nachricht von DW!");
        assertThat(output).contains("PAYLOAD: DWNewsCreated(id=1, titel=titel)");
    }

    @Test
    void testRAI(CapturedOutput output) {
        NewsEvent newsEvent = new RAINewsCreated("1", "titolo");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder
                .withData(newsEvent)
                .setHeader(PARTITION_KEY, "rai")
                .setHeader(TYPE, "rai")
                .build();

        inputDestination.send(newsEventMessage, DESTINATION_NAME);

        assertThat(output).contains("Ricevuta notizia creata messaggio da RAI!");
        assertThat(output).contains("PAYLOAD: RAINewsCreated(id=1, titolo=titolo)");
    }

    private static final String DESTINATION_NAME = "news.events";
    private static final String PARTITION_KEY = "partitionKey";
    private static final String TYPE = "type";
}