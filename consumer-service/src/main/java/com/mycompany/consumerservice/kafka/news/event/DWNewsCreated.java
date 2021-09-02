package com.mycompany.consumerservice.kafka.news.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DWNewsCreated implements NewsEvent {
    private String id;
    private String titel;
}
