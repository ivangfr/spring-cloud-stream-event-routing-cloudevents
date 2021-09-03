package com.mycompany.producerservice.rest.alert;

import com.mycompany.producerservice.kafka.alert.AlertEventProducer;
import com.mycompany.producerservice.kafka.alert.AlertType;
import com.mycompany.producerservice.kafka.alert.event.AlertEvent;
import com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.producerservice.kafka.alert.event.WeatherAlert;
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
@WebFluxTest(AlertController.class)
class AlertControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AlertEventProducer alertEventProducer;

    @Test
    void testCreateEarthquakeAlert() {
        AlertEvent alertEvent = EarthquakeAlert.of("1", 2.1, 1.0, -1.0);
        Message<AlertEvent> alertEventMessage = CloudEventMessageBuilder.withData(alertEvent).build();

        given(alertEventProducer.send(any(AlertType.class), any(AlertEvent.class))).willReturn(alertEventMessage);

        CreateEarthquakeAlertRequest request = new CreateEarthquakeAlertRequest(2.1, 1.0, -1.0);

        webTestClient.post()
                .uri(BASE_URL + "/earthquake")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateEarthquakeAlertRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(response -> assertThat(
                        response.contains("\"payload\":{\"id\":\"1\",\"richterScale\":2.1,\"epicenterLat\":1.0,\"epicenterLon\":-1.0}"))
                        .isTrue());
    }

    @Test
    void testCreateWeatherAlert() {
        AlertEvent alertEvent = WeatherAlert.of("1", "message");
        Message<AlertEvent> alertEventMessage = CloudEventMessageBuilder.withData(alertEvent).build();

        given(alertEventProducer.send(any(AlertType.class), any(AlertEvent.class))).willReturn(alertEventMessage);

        CreateWeatherAlertRequest request = new CreateWeatherAlertRequest("message");

        webTestClient.post()
                .uri(BASE_URL + "/weather")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateEarthquakeAlertRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(response ->
                        assertThat(response.contains("\"payload\":{\"id\":\"1\",\"message\":\"message\"}")).isTrue());
    }

    private static final String BASE_URL = "/api/alerts";
}