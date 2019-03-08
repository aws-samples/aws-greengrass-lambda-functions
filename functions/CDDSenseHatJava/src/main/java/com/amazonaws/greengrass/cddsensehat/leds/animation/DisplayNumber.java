package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDOrientation;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDPartialImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.inject.Inject;

/**
 * Created by timmatt on 3/6/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DisplayNumber implements Animation {
    private final Characters characters;

    @Getter
    long period = 5000;

    @Setter
    @Getter
    private int versionNumber = 0;

    @Override
    public SenseHatLEDImage nextImage() {
        SenseHatLEDPartialImage v = characters.get('v').getPartialImage();

        SenseHatLEDImage temp;

        SenseHatLEDPartialImage onesNumber = characters.get((char) ('0' + versionNumber)).getPartialImage();

        temp = new SenseHatLEDImage();
        temp = v.merge(0, 0, temp);
        temp = onesNumber.merge(4, 0, temp);

        temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.NORMAL);

        return temp;
    }
}
