package com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;

public interface Animation {
    long getPeriod();

    void reset();

    SenseHatLEDImage nextImage();
}
