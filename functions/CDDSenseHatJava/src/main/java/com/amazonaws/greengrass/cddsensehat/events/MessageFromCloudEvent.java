package com.amazonaws.greengrass.cddsensehat.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageFromCloudEvent {
    private String topic;

    private String message;
}
