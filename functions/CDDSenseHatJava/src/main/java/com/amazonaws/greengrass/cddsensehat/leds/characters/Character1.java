package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

/**
 * Created by timmatt on 2/21/17.
 */
public class Character1 extends AbstractCharacter {
    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, black, black, white, black);
        setLine(temp, 1, black, white, white, black);
        setLine(temp, 2, black, black, white, black);
        setLine(temp, 3, black, black, white, black);
        setLine(temp, 4, black, black, white, black);
        setLine(temp, 5, black, black, white, black);
        setLine(temp, 6, black, black, white, black);
        setLine(temp, 7, white, white, white, white);
    }
}
