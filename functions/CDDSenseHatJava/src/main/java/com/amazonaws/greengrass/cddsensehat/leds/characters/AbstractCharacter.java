package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.AbstractImage;

public abstract class AbstractCharacter extends AbstractImage {
    private static final int HEIGHT = 8;
    private static final int WIDTH = 4;

    protected final int getHeight() {
        return HEIGHT;
    }

    protected final int getWidth() {
        return WIDTH;
    }
}
