package com.ivanfranchin.producerservice.rest.alert;

import com.ivanfranchin.producerservice.kafka.alert.AlertEventProducer;
import com.ivanfranchin.producerservice.kafka.alert.event.EarthquakeAlert;
import com.ivanfranchin.producerservice.kafka.alert.event.WeatherAlert;
import com.ivanfranchin.producerservice.rest.alert.dto.CreateEarthquakeAlertRequest;
import com.ivanfranchin.producerservice.rest.alert.dto.CreateWeatherAlertRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AlertController.class)
class AlertControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AlertEventProducer alertEventProducer;

    @Test
    void testCreateEarthquakeAlert() {
        CreateEarthquakeAlertRequest request = new CreateEarthquakeAlertRequest(2.1, 1.0, -1.0);

        webTestClient.post()
                .uri(BASE_URL + "/earthquake")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateEarthquakeAlertRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EarthquakeAlert.class)
                .value(response -> {
                    assertThat(response.id()).isNotNull();
                    assertThat(response.richterScale()).isEqualTo(request.richterScale());
                    assertThat(response.epicenterLat()).isEqualTo(request.epicenterLat());
                    assertThat(response.epicenterLon()).isEqualTo(request.epicenterLon());
                });
    }

    @Test
    void testCreateWeatherAlert() {
        CreateWeatherAlertRequest request = new CreateWeatherAlertRequest("message");

        webTestClient.post()
                .uri(BASE_URL + "/weather")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateEarthquakeAlertRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(WeatherAlert.class)
                .value(response -> {
                    assertThat(response.id()).isNotNull();
                    assertThat(response.message()).isEqualTo(request.message());
                });
    }

    private static final String BASE_URL = "/api/alerts";
}