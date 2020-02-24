package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;

import javax.inject.Inject;

public class Blank implements Animation {
    @Inject
    public Blank() {
    }

    @Override
    public long getPeriod() {
        return 5000;
    }

    @Override
    public void reset() {
        // Do nothing
    }

    @Override
    public SenseHatLEDImage nextImage() {
        return new SenseHatLEDImage();
    }
}
