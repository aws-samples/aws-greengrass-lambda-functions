package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

public class Character5 extends AbstractCharacter {
    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, white, white, white, white);
        setLine(temp, 1, white, black, black, black);
        setLine(temp, 2, white, black, black, black);
        setLine(temp, 3, black, white, white, black);
        setLine(temp, 4, black, black, black, white);
        setLine(temp, 5, black, black, black, white);
        setLine(temp, 6, white, black, black, white);
        setLine(temp, 7, black, white, white, black);
    }
}
