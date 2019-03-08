package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.AbstractImage;

/**
 * Created by timmatt on 2/21/17.
 */
public abstract class AbstractCharacter extends AbstractImage {
    public static final int HEIGHT = 8;
    public static final int WIDTH = 4;

    protected final int getHeight() {
        return HEIGHT;
    }

    protected final int getWidth() {
        return WIDTH;
    }
}
