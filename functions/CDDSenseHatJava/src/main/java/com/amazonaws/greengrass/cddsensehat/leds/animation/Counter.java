package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDPartialImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

/**
 * Created by timmatt on 2/21/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Counter implements Animation {
    private final Characters characters;

    @Getter
    long period = 10;

    private long counter = 0;

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
