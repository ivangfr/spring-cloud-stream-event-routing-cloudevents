package com.mycompany.consumerservice.kafka.news.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CNNNewsCreated implements NewsEvent {
    private String id;
    private String title;
}
