package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

public class Character0 extends AbstractCharacter {
    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, black, white, white, black);
        setLine(temp, 1, white, black, black, white);
        setLine(temp, 2, white, black, black, white);
        setLine(temp, 3, white, black, black, white);
        setLine(temp, 4, white, black, black, white);
        setLine(temp, 5, white, black, black, white);
        setLine(temp, 6, white, black, black, white);
        setLine(temp, 7, black, white, white, black);
    }
}
