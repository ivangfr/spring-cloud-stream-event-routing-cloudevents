package com.mycompany.producerservice.rest.alert;

import com.mycompany.producerservice.kafka.alert.AlertEventProducer;
import com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.producerservice.kafka.alert.event.WeatherAlert;
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
@WebFluxTest(AlertController.class)
class AlertControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
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
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getRichterScale()).isEqualTo(request.getRichterScale());
                    assertThat(response.getEpicenterLat()).isEqualTo(request.getEpicenterLat());
                    assertThat(response.getEpicenterLon()).isEqualTo(request.getEpicenterLon());
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
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getMessage()).isEqualTo(request.getMessage());
                });
    }

    private static final String BASE_URL = "/api/alerts";
}