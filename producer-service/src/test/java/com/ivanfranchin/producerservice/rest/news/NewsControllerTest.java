package com.ivanfranchin.producerservice.rest.news;

import com.ivanfranchin.producerservice.kafka.news.NewsEventProducer;
import com.ivanfranchin.producerservice.kafka.news.event.CNNNewsCreated;
import com.ivanfranchin.producerservice.kafka.news.event.DWNewsCreated;
import com.ivanfranchin.producerservice.kafka.news.event.RAINewsCreated;
import com.ivanfranchin.producerservice.rest.news.dto.CreateCNNNewsRequest;
import com.ivanfranchin.producerservice.rest.news.dto.CreateDWNewsRequest;
import com.ivanfranchin.producerservice.rest.news.dto.CreateRAINewsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebFluxTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NewsEventProducer newsEventProducer;

    @Test
    void testCreateCNNNews() {
        CreateCNNNewsRequest request = new CreateCNNNewsRequest("title");

        webTestClient.post()
                .uri(BASE_URL + "/cnn")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateCNNNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CNNNewsCreated.class)
                .value(response -> {
                    assertThat(response.id()).isNotNull();
                    assertThat(response.title()).isEqualTo(request.title());
                });
    }

    @Test
    void testCreateDWNews() {
        CreateDWNewsRequest request = new CreateDWNewsRequest("titel");

        webTestClient.post()
                .uri(BASE_URL + "/dw")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateDWNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DWNewsCreated.class)
                .value(response -> {
                    assertThat(response.id()).isNotNull();
                    assertThat(response.titel()).isEqualTo(request.titel());
                });
    }

    @Test
    void testCreateRAINews() {
        CreateRAINewsRequest request = new CreateRAINewsRequest("titolo");

        webTestClient.post()
                .uri(BASE_URL + "/rai")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateDWNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RAINewsCreated.class)
                .value(response -> {
                    assertThat(response.id()).isNotNull();
                    assertThat(response.titolo()).isEqualTo(request.titolo());
                });
    }

    private static final String BASE_URL = "/api/news";
}