package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

public class CharacterUpperV extends AbstractCharacter {
    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, black, black, black, black);
        setLine(temp, 1, black, black, black, black);
        setLine(temp, 2, black, black, black, black);
        setLine(temp, 3, black, black, black, black);
        setLine(temp, 4, white, black, black, white);
        setLine(temp, 5, white, black, black, white);
        setLine(temp, 6, white, black, white, black);
        setLine(temp, 7, black, white, black, black);
    }
}
