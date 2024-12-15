package com.ivanfranchin.consumerservice.news;

import com.ivanfranchin.consumerservice.news.event.CNNNewsCreated;
import com.ivanfranchin.consumerservice.news.event.DWNewsCreated;
import com.ivanfranchin.consumerservice.news.event.NewsEvent;
import com.ivanfranchin.consumerservice.news.event.RAINewsCreated;
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
    void testCNNNewsCreated(CapturedOutput output) {
        NewsEvent newsEvent = new CNNNewsCreated("id", "title");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder
                .withData(newsEvent)
                .setType("com.ivanfranchin.producerservice.news.event.CNNNewsCreated")
                .setHeader(PARTITION_KEY, "id")
                .build();

        inputDestination.send(newsEventMessage, DESTINATION_NAME);

        assertThat(output).contains("Received news created message from CNN!");
        assertThat(output).contains("PAYLOAD: CNNNewsCreated[id=id, title=title]");
    }

    @Test
    void testDWNewsCreated(CapturedOutput output) {
        NewsEvent newsEvent = new DWNewsCreated("id", "titel");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder
                .withData(newsEvent)
                .setType("com.ivanfranchin.producerservice.news.event.DWNewsCreated")
                .setHeader(PARTITION_KEY, "id")
                .build();

        inputDestination.send(newsEventMessage, DESTINATION_NAME);

        assertThat(output).contains("Erhaltene Nachrichten erstellte Nachricht von DW!");
        assertThat(output).contains("PAYLOAD: DWNewsCreated[id=id, titel=titel]");
    }

    @Test
    void testRAINewsCreated(CapturedOutput output) {
        NewsEvent newsEvent = new RAINewsCreated("id", "titolo");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder
                .withData(newsEvent)
                .setType("com.ivanfranchin.producerservice.news.event.RAINewsCreated")
                .setHeader(PARTITION_KEY, "id")
                .build();

        inputDestination.send(newsEventMessage, DESTINATION_NAME);

        assertThat(output).contains("Ricevuta notizia creata messaggio da RAI!");
        assertThat(output).contains("PAYLOAD: RAINewsCreated[id=id, titolo=titolo]");
    }

    private static final String DESTINATION_NAME = "news.events";
    private static final String PARTITION_KEY = "partitionKey";
}