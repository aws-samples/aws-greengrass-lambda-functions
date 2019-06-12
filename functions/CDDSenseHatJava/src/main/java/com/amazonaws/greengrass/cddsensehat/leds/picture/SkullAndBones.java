package com.amazonaws.greengrass.cddsensehat.leds.picture;

import com.amazonaws.greengrass.cddsensehat.leds.AbstractImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

public class SkullAndBones extends AbstractImage {
    @Override
    protected int getWidth() {
        return 8;
    }

    @Override
    protected int getHeight() {
        return 8;
    }

    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, black, white, white, white, white, white, white, black);
        setLine(temp, 1, white, black, white, black, black, white, black, white);
        setLine(temp, 2, white, white, black, white, white, black, white, white);
        setLine(temp, 3, white, black, white, black, black, white, black, white);
        setLine(temp, 4, black, white, white, white, white, white, white, black);
        setLine(temp, 5, white, white, white, black, black, white, white, white);
        setLine(temp, 6, black, black, black, white, white, black, black, black);
        setLine(temp, 7, white, white, white, black, black, white, white, white);
    }
}
