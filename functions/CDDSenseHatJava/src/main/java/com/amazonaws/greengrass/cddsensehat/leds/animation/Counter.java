package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDPartialImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;

import javax.inject.Inject;

public class Counter implements Animation {
    @Inject
    Characters characters;
    private long counter = 0;

    @Inject
    public Counter() {
    }

    @Override
    public void reset() {
        counter = 0;
    }

    @Override
    public long getPeriod() {
        return 10;
    }

    @Override
    public SenseHatLEDImage nextImage() {
        int tens = (int) (counter / 10);
        int ones = (int) (counter % 10);
        SenseHatLEDPartialImage tensNumber = characters.get((char) (tens + '0')).getPartialImage();
        SenseHatLEDPartialImage onesNumber = characters.get((char) (ones + '0')).getPartialImage();
        SenseHatLEDImage temp = new SenseHatLEDImage();
        temp = tensNumber.merge(0, 0, temp);
        temp = onesNumber.merge(4, 0, temp);

        counter++;
        return temp;
    }
}
