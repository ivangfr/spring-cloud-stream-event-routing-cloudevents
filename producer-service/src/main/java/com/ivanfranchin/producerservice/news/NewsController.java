package com.ivanfranchin.producerservice.news;

import com.ivanfranchin.producerservice.news.dto.CreateCNNNewsRequest;
import com.ivanfranchin.producerservice.news.dto.CreateDWNewsRequest;
import com.ivanfranchin.producerservice.news.dto.CreateRAINewsRequest;
import com.ivanfranchin.producerservice.news.event.CNNNewsCreated;
import com.ivanfranchin.producerservice.news.event.DWNewsCreated;
import com.ivanfranchin.producerservice.news.event.RAINewsCreated;
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

    private final NewsEventEmitter newsEventEmitter;

    public NewsController(NewsEventEmitter newsEventEmitter) {
        this.newsEventEmitter = newsEventEmitter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cnn")
    public Mono<CNNNewsCreated> createCNNNews(@Valid @RequestBody CreateCNNNewsRequest request) {
        CNNNewsCreated cnnNewsCreated = new CNNNewsCreated(getId(), request.title());
        newsEventEmitter.send(cnnNewsCreated.id(), cnnNewsCreated);
        return Mono.just(cnnNewsCreated);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/dw")
    public Mono<DWNewsCreated> createDWNews(@Valid @RequestBody CreateDWNewsRequest request) {
        DWNewsCreated dwNewsCreated = new DWNewsCreated(getId(), request.titel());
        newsEventEmitter.send(dwNewsCreated.id(), dwNewsCreated);
        return Mono.just(dwNewsCreated);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rai")
    public Mono<RAINewsCreated> createRAINews(@Valid @RequestBody CreateRAINewsRequest request) {
        RAINewsCreated raiNewsCreated = new RAINewsCreated(getId(), request.titolo());
        newsEventEmitter.send(raiNewsCreated.id(), raiNewsCreated);
        return Mono.just(raiNewsCreated);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
