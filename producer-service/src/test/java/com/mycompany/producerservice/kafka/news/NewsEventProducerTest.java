package com.mycompany.producerservice.kafka.news;

import com.mycompany.producerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.producerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.producerservice.kafka.news.event.RAINewsCreated;
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

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class NewsEventProducerTest {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private NewsEventProducer newsEventProducer;

    @Test
    void testSendCNNNewsCreated() {
        newsEventProducer.send(BroadcasterType.CNN, CNNNewsCreated.of("1", "title"));

        Message<byte[]> outputMessage = outputDestination.receive(0, "news.events");
        MessageHeaders headers = outputMessage.getHeaders();
        String payloadStr = new String(outputMessage.getPayload(), StandardCharsets.UTF_8);

        assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
        assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
        assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(CNNNewsCreated.class.getName());
        assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("cnn");
        assertThat(headers.get(TYPE)).isEqualTo("cnn");
        assertThat(payloadStr).isEqualTo("{\"id\":\"1\",\"title\":\"title\"}");
    }

    @Test
    void testSendDWNewsCreated() {
        newsEventProducer.send(BroadcasterType.DW, DWNewsCreated.of("1", "titel"));

        Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
        MessageHeaders headers = outputMessage.getHeaders();
        String payloadStr = new String(outputMessage.getPayload(), StandardCharsets.UTF_8);

        assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
        assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
        assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(DWNewsCreated.class.getName());
        assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("dw");
        assertThat(headers.get(TYPE)).isEqualTo("dw");
        assertThat(payloadStr).isEqualTo("{\"id\":\"1\",\"titel\":\"titel\"}");
    }

    @Test
    void testSendRAINewsCreated() {
        newsEventProducer.send(BroadcasterType.RAI, RAINewsCreated.of("1", "titolo"));

        Message<byte[]> outputMessage = outputDestination.receive(0, BINDING_NAME);
        MessageHeaders headers = outputMessage.getHeaders();
        String payloadStr = new String(outputMessage.getPayload(), StandardCharsets.UTF_8);

        assertThat(headers.get(CloudEventMessageUtils.SOURCE)).isEqualTo(SOURCE_URI);
        assertThat(headers.get(CloudEventMessageUtils.SPECVERSION)).isEqualTo(VERSION_1_0);
        assertThat(headers.get(CloudEventMessageUtils.TYPE)).isEqualTo(RAINewsCreated.class.getName());
        assertThat(headers.get(CloudEventMessageUtils.ID)).isNotNull();
        assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(headers.get(PARTITION_KEY)).isEqualTo("rai");
        assertThat(headers.get(TYPE)).isEqualTo("rai");
        assertThat(payloadStr).isEqualTo("{\"id\":\"1\",\"titolo\":\"titolo\"}");
    }

    private static final String BINDING_NAME = "news.events";
    private static final String VERSION_1_0 = "1.0";
    private static final URI SOURCE_URI = URI.create("https://spring.io/");
    private static final String PARTITION_KEY = "partitionKey";
    private static final String TYPE = "type";
}