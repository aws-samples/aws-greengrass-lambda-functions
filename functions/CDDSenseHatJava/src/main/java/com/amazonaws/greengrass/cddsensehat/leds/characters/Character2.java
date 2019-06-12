package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

public class Character2 extends AbstractCharacter {
    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, black, white, white, black);
        setLine(temp, 1, white, black, black, white);
        setLine(temp, 2, black, black, black, white);
        setLine(temp, 3, black, black, white, black);
        setLine(temp, 4, black, white, black, black);
        setLine(temp, 5, white, black, black, black);
        setLine(temp, 6, white, black, black, black);
        setLine(temp, 7, white, white, white, white);
    }
}
