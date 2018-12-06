package com.amazonaws.greengrass.cddembeddedvaadinskeleton.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageFromCloudEvent {
    private String topic;

    private String message;
}
