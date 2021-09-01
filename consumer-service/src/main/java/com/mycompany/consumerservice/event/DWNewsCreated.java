package com.mycompany.consumerservice.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class DWNewsCreated implements NewsEvent {
    String id;
    String titel;
}
