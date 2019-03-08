package com.amazonaws.greengrass.cddsensehat.leds.characters;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;

/**
 * Created by timmatt on 2/21/17.
 */
public class Character7 extends AbstractCharacter {
    @Override
    protected void draw(SenseHatLED[][] temp) {
        setLine(temp, 0, white, white, white, white);
        setLine(temp, 1, black, black, black, white);
        setLine(temp, 2, black, black, black, white);
        setLine(temp, 3, black, black, white, black);
        setLine(temp, 4, black, white, black, black);
        setLine(temp, 5, black, white, black, black);
        setLine(temp, 6, black, white, black, black);
        setLine(temp, 7, black, white, black, black);
    }
}
