package com.mycompany.producerservice.rest;

import com.mycompany.producerservice.event.CNNNewsCreated;
import com.mycompany.producerservice.event.DWNewsCreated;
import com.mycompany.producerservice.event.RAINewsCreated;
import com.mycompany.producerservice.kafka.Broadcaster;
import com.mycompany.producerservice.kafka.NewsEventProducer;
import com.mycompany.producerservice.rest.dto.CreateCNNNewsRequest;
import com.mycompany.producerservice.rest.dto.CreateDWNewsRequest;
import com.mycompany.producerservice.rest.dto.CreateRAINewsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;

    @PostMapping("/cnn")
    public void createCNNNews(@Valid @RequestBody CreateCNNNewsRequest createCNNNewsRequest) {
        CNNNewsCreated cnnNewsCreated = CNNNewsCreated.of(getId(), createCNNNewsRequest.getTitle());
        newsEventProducer.send(Broadcaster.CNN, cnnNewsCreated);
    }

    @PostMapping("/dw")
    public void createDWNews(@Valid @RequestBody CreateDWNewsRequest createDWNewsRequest) {
        DWNewsCreated dwNewsCreated = DWNewsCreated.of(getId(), createDWNewsRequest.getTitel());
        newsEventProducer.send(Broadcaster.DW, dwNewsCreated);
    }

    @PostMapping("rai")
    public void createRAINews(@Valid @RequestBody CreateRAINewsRequest createRAINewsRequest) {
        RAINewsCreated raiNewsCreated = RAINewsCreated.of(getId(), createRAINewsRequest.getTitolo());
        newsEventProducer.send(Broadcaster.RAI, raiNewsCreated);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
