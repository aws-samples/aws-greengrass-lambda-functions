package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDOrientation;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDPartialImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;

import javax.inject.Inject;

public class DisplayNumber implements Animation {
    @Inject
    Characters characters;
    private int versionNumber = 0;

    @Inject
    public DisplayNumber() {
    }

    @Override
    public long getPeriod() {
        return 5000;
    }

    @Override
    public void reset() {
        // Do nothing
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

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
