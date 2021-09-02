package com.mycompany.producerservice.rest.news;

import com.mycompany.producerservice.kafka.news.BroadcasterType;
import com.mycompany.producerservice.kafka.news.NewsEventProducer;
import com.mycompany.producerservice.kafka.news.event.CNNNewsCreated;
import com.mycompany.producerservice.kafka.news.event.DWNewsCreated;
import com.mycompany.producerservice.kafka.news.event.NewsEvent;
import com.mycompany.producerservice.kafka.news.event.RAINewsCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;

    @PostMapping("/cnn")
    public Message<NewsEvent> createCNNNews(@Valid @RequestBody CreateCNNNewsRequest request) {
        CNNNewsCreated cnnNewsCreated = CNNNewsCreated.of(getId(), request.getTitle());
        return newsEventProducer.send(BroadcasterType.CNN, cnnNewsCreated);
    }

    @PostMapping("/dw")
    public Message<NewsEvent> createDWNews(@Valid @RequestBody CreateDWNewsRequest request) {
        DWNewsCreated dwNewsCreated = DWNewsCreated.of(getId(), request.getTitel());
        return newsEventProducer.send(BroadcasterType.DW, dwNewsCreated);
    }

    @PostMapping("rai")
    public Message<NewsEvent> createRAINews(@Valid @RequestBody CreateRAINewsRequest request) {
        RAINewsCreated raiNewsCreated = RAINewsCreated.of(getId(), request.getTitolo());
        return newsEventProducer.send(BroadcasterType.RAI, raiNewsCreated);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
