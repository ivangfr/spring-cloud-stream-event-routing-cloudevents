package com.ivanfranchin.producerservice.kafka.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.producerservice.kafka.news.event.CNNNewsCreated;
import com.ivanfranchin.producerservice.kafka.news.event.DWNewsCreated;
import com.ivanfranchin.producerservice.kafka.news.event.RAINewsCreated;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class NewsEventProducerTest {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private NewsEventProducer newsEventProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSendCNNNewsCreated() throws IOException {
        CNNNewsCreated cnnNewsCreated = new CNNNewsCreated("id", "title");
        newsEventProducer.send(cnnNewsCreated.id(), cnnNewsCreated);

        Message<byte[]> outputMessage = outputDestination.receive(0, "news.events");
        MessageHeaders headers = outputMessage.getHeaders();
        CNNNewsCreated payload = objectMapper.readValue(outputMessage.getPayload(), CNNNewsCreated.class);

        assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
        assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
        assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(CNNNewsCreated.class.getName());
        assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

        assertThat(payload.id()).isEqualTo(cnnNewsCreated.id());
        assertThat(payload.title()).isEqualTo(cnnNewsCreated.title());
    }

    @Test
    void testSendDWNewsCreated() throws IOException {
        DWNewsCreated dwNewsCreated = new DWNewsCreated("id", "titel");
        newsEventProducer.send(dwNewsCreated.id(), dwNewsCreated);

        Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
        MessageHeaders headers = outputMessage.getHeaders();
        DWNewsCreated payload = objectMapper.readValue(outputMessage.getPayload(), DWNewsCreated.class);

        assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
        assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
        assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(DWNewsCreated.class.getName());
        assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

        assertThat(payload.id()).isEqualTo(dwNewsCreated.id());
        assertThat(payload.titel()).isEqualTo(dwNewsCreated.titel());
    }

    @Test
    void testSendRAINewsCreated() throws IOException {
        RAINewsCreated raiNewsCreated = new RAINewsCreated("id", "titolo");
        newsEventProducer.send(raiNewsCreated.id(), raiNewsCreated);

        Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
        MessageHeaders headers = outputMessage.getHeaders();
        RAINewsCreated payload = objectMapper.readValue(outputMessage.getPayload(), RAINewsCreated.class);

        assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
        assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
        assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(RAINewsCreated.class.getName());
        assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

        assertThat(payload.id()).isEqualTo(raiNewsCreated.id());
        assertThat(payload.titolo()).isEqualTo(raiNewsCreated.titolo());
    }

    private static final String BINDING_NAME = "news.events";
    private static final String VERSION_1_0 = "1.0";
    private static final URI SOURCE_URI = URI.create("https://spring.io/");
    private static final String PARTITION_KEY = "partitionKey";
}