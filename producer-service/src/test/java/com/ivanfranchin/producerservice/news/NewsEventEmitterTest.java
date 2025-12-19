package com.ivanfranchin.producerservice.news;

import com.ivanfranchin.producerservice.news.event.CNNNewsCreated;
import com.ivanfranchin.producerservice.news.event.DWNewsCreated;
import com.ivanfranchin.producerservice.news.event.RAINewsCreated;
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
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class NewsEventEmitterTest {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private NewsEventEmitter newsEventEmitter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSendCNNNewsCreated() throws IOException {
        CNNNewsCreated cnnNewsCreated = new CNNNewsCreated("id", "title");
        newsEventEmitter.send(cnnNewsCreated.id(), cnnNewsCreated);

        Message<byte[]> outputMessage = outputDestination.receive(0, "news.events");

        MessageHeaders headers = outputMessage.getHeaders();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

        assertThat(CloudEventMessageUtils.getSource(outputMessage)).isEqualTo(SOURCE_URI);
        assertThat(CloudEventMessageUtils.getSpecVersion(outputMessage)).isEqualTo(VERSION_1_0);
        assertThat(CloudEventMessageUtils.getType(outputMessage)).isEqualTo(CNNNewsCreated.class.getName());
        assertThat(CloudEventMessageUtils.getId(outputMessage)).isNotNull();

        CNNNewsCreated payload = objectMapper.readValue(outputMessage.getPayload(), CNNNewsCreated.class);
        assertThat(payload.id()).isEqualTo(cnnNewsCreated.id());
        assertThat(payload.title()).isEqualTo(cnnNewsCreated.title());
    }

    @Test
    void testSendDWNewsCreated() throws IOException {
        DWNewsCreated dwNewsCreated = new DWNewsCreated("id", "titel");
        newsEventEmitter.send(dwNewsCreated.id(), dwNewsCreated);

        Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);

        MessageHeaders headers = outputMessage.getHeaders();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

        assertThat(CloudEventMessageUtils.getSource(outputMessage)).isEqualTo(SOURCE_URI);
        assertThat(CloudEventMessageUtils.getSpecVersion(outputMessage)).isEqualTo(VERSION_1_0);
        assertThat(CloudEventMessageUtils.getType(outputMessage)).isEqualTo(DWNewsCreated.class.getName());
        assertThat(CloudEventMessageUtils.getId(outputMessage)).isNotNull();

        DWNewsCreated payload = objectMapper.readValue(outputMessage.getPayload(), DWNewsCreated.class);
        assertThat(payload.id()).isEqualTo(dwNewsCreated.id());
        assertThat(payload.titel()).isEqualTo(dwNewsCreated.titel());
    }

    @Test
    void testSendRAINewsCreated() throws IOException {
        RAINewsCreated raiNewsCreated = new RAINewsCreated("id", "titolo");
        newsEventEmitter.send(raiNewsCreated.id(), raiNewsCreated);

        Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);

        MessageHeaders headers = outputMessage.getHeaders();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("id");

        assertThat(CloudEventMessageUtils.getSource(outputMessage)).isEqualTo(SOURCE_URI);
        assertThat(CloudEventMessageUtils.getSpecVersion(outputMessage)).isEqualTo(VERSION_1_0);
        assertThat(CloudEventMessageUtils.getType(outputMessage)).isEqualTo(RAINewsCreated.class.getName());
        assertThat(CloudEventMessageUtils.getId(outputMessage)).isNotNull();

        RAINewsCreated payload = objectMapper.readValue(outputMessage.getPayload(), RAINewsCreated.class);
        assertThat(payload.id()).isEqualTo(raiNewsCreated.id());
        assertThat(payload.titolo()).isEqualTo(raiNewsCreated.titolo());
    }

    private static final String BINDING_NAME = "news.events";
    private static final String VERSION_1_0 = "1.0";
    private static final URI SOURCE_URI = URI.create("https://spring.io/");
    private static final String PARTITION_KEY = "partitionKey";
}