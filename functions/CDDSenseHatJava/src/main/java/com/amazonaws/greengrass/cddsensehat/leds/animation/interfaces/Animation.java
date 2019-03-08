package com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;

/**
 * Created by timmatt on 3/6/17.
 */
public interface Animation {
    long getPeriod();

    SenseHatLEDImage nextImage();
}
