package com.mycompany.producerservice.rest.news;

import com.mycompany.producerservice.kafka.news.BroadcasterType;
import com.mycompany.producerservice.kafka.news.NewsEventProducer;
import com.mycompany.producerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.producerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.producerservice.kafka.news.event.NewsEvent;
import com.mycompany.producerservice.kafka.news.event.RAINewsCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NewsEventProducer newsEventProducer;

    @Test
    void testCreateCNNNews() {
        NewsEvent newsEvent = CNNNewsCreated.of("1", "title");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder.withData(newsEvent).build();

        given(newsEventProducer.send(any(BroadcasterType.class), any(NewsEvent.class))).willReturn(newsEventMessage);

        CreateCNNNewsRequest request = new CreateCNNNewsRequest("title");

        webTestClient.post()
                .uri(BASE_URL + "/cnn")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateCNNNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(response ->
                        assertThat(response.contains("\"payload\":{\"id\":\"1\",\"title\":\"title\"}")).isTrue());
    }

    @Test
    void testCreateDWNews() {
        NewsEvent newsEvent = DWNewsCreated.of("1", "titel");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder.withData(newsEvent).build();

        given(newsEventProducer.send(any(BroadcasterType.class), any(NewsEvent.class))).willReturn(newsEventMessage);

        CreateDWNewsRequest request = new CreateDWNewsRequest("titel");

        webTestClient.post()
                .uri(BASE_URL + "/dw")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateDWNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(response ->
                        assertThat(response.contains("\"payload\":{\"id\":\"1\",\"titel\":\"titel\"}")).isTrue());
    }

    @Test
    void testCreateRAINews() {
        NewsEvent newsEvent = RAINewsCreated.of("1", "titolo");
        Message<NewsEvent> newsEventMessage = CloudEventMessageBuilder.withData(newsEvent).build();

        given(newsEventProducer.send(any(BroadcasterType.class), any(NewsEvent.class))).willReturn(newsEventMessage);

        CreateRAINewsRequest request = new CreateRAINewsRequest("titolo");

        webTestClient.post()
                .uri(BASE_URL + "/rai")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateDWNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(response ->
                        assertThat(response.contains("\"payload\":{\"id\":\"1\",\"titolo\":\"titolo\"}")).isTrue());
    }

    private static final String BASE_URL = "/api/news";
}