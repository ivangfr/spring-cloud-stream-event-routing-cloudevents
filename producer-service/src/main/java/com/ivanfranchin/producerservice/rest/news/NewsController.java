package com.ivanfranchin.producerservice.rest.news;

import com.ivanfranchin.producerservice.kafka.news.NewsEventProducer;
import com.ivanfranchin.producerservice.kafka.news.event.CNNNewsCreated;
import com.ivanfranchin.producerservice.kafka.news.event.DWNewsCreated;
import com.ivanfranchin.producerservice.kafka.news.event.RAINewsCreated;
import com.ivanfranchin.producerservice.rest.news.dto.CreateCNNNewsRequest;
import com.ivanfranchin.producerservice.rest.news.dto.CreateDWNewsRequest;
import com.ivanfranchin.producerservice.rest.news.dto.CreateRAINewsRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;

    public NewsController(NewsEventProducer newsEventProducer) {
        this.newsEventProducer = newsEventProducer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cnn")
    public Mono<CNNNewsCreated> createCNNNews(@Valid @RequestBody CreateCNNNewsRequest request) {
        CNNNewsCreated cnnNewsCreated = new CNNNewsCreated(getId(), request.title());
        newsEventProducer.send(cnnNewsCreated.id(), cnnNewsCreated);
        return Mono.just(cnnNewsCreated);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/dw")
    public Mono<DWNewsCreated> createDWNews(@Valid @RequestBody CreateDWNewsRequest request) {
        DWNewsCreated dwNewsCreated = new DWNewsCreated(getId(), request.titel());
        newsEventProducer.send(dwNewsCreated.id(), dwNewsCreated);
        return Mono.just(dwNewsCreated);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rai")
    public Mono<RAINewsCreated> createRAINews(@Valid @RequestBody CreateRAINewsRequest request) {
        RAINewsCreated raiNewsCreated = new RAINewsCreated(getId(), request.titolo());
        newsEventProducer.send(raiNewsCreated.id(), raiNewsCreated);
        return Mono.just(raiNewsCreated);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
