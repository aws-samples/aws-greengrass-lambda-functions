package com.timmattison.greengrass.cdd.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishBinaryEvent {
    private String topic;
    private byte[] bytes;
}
