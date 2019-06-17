package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;

public class Blank implements Animation {
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
