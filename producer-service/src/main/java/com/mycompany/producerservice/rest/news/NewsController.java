package com.mycompany.producerservice.rest.news;

import com.mycompany.producerservice.kafka.news.BroadcasterType;
import com.mycompany.producerservice.kafka.news.NewsEventProducer;
import com.mycompany.producerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.producerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.producerservice.kafka.news.event.NewsEvent;
import com.mycompany.producerservice.kafka.news.event.RAINewsCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cnn")
    public Mono<Message<NewsEvent>> createCNNNews(@Valid @RequestBody CreateCNNNewsRequest request) {
        CNNNewsCreated cnnNewsCreated = CNNNewsCreated.of(getId(), request.getTitle());
        Message<NewsEvent> newsEventMessage = newsEventProducer.send(BroadcasterType.CNN, cnnNewsCreated);
        return Mono.just(newsEventMessage);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/dw")
    public Mono<Message<NewsEvent>> createDWNews(@Valid @RequestBody CreateDWNewsRequest request) {
        DWNewsCreated dwNewsCreated = DWNewsCreated.of(getId(), request.getTitel());
        Message<NewsEvent> newsEventMessage = newsEventProducer.send(BroadcasterType.DW, dwNewsCreated);
        return Mono.just(newsEventMessage);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rai")
    public Mono<Message<NewsEvent>> createRAINews(@Valid @RequestBody CreateRAINewsRequest request) {
        RAINewsCreated raiNewsCreated = RAINewsCreated.of(getId(), request.getTitolo());
        Message<NewsEvent> newsEventMessage = newsEventProducer.send(BroadcasterType.RAI, raiNewsCreated);
        return Mono.just(newsEventMessage);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
