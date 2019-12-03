package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDOrientation;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDPartialImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SpinNumber implements Animation {
    private final Logger log = LoggerFactory.getLogger(SpinNumber.class);
    @Inject
    Characters characters;
    private int versionNumber = 0;
    private long counter = 0;

    @Inject
    public SpinNumber() {
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public long getPeriod() {
        return 5000;
    }

    @Override
    public void reset() {
        // Do nothing
    }

    @Override
    public SenseHatLEDImage nextImage() {
        SenseHatLEDPartialImage v = characters.get('v').getPartialImage();

        SenseHatLEDImage temp;

        SenseHatLEDPartialImage onesNumber = characters.get((char) ('0' + versionNumber)).getPartialImage();

        temp = new SenseHatLEDImage();
        temp = v.merge(0, 0, temp);
        temp = onesNumber.merge(4, 0, temp);

        SenseHatLEDOrientation orientation = SenseHatLEDOrientation.values()[(int) (counter % SenseHatLEDOrientation.values().length)];
        temp.setSenseHatLEDOrientation(orientation);
        log.info(String.valueOf(orientation));

        counter++;
        return temp;
    }
}
