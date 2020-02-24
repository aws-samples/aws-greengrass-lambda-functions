package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDOrientation;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDPartialImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;

import javax.inject.Inject;
import java.util.Random;

public class GlitchNumber implements Animation {
    private static final int GLITCH_RANDOM_LIMIT = 250;
    private final Random random = new Random();
    @Inject
    Characters characters;

    @Inject
    public GlitchNumber() {
    }

    private int versionNumber = 0;

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public long getPeriod() {
        return 10;
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

        temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.NORMAL);

        SenseHatLED[] tempLEDs = temp.getSenseHatLEDs();

        for (int loop = 0; loop < 8; loop++) {
            int glitch = random.nextInt(GLITCH_RANDOM_LIMIT);

            if (glitch == 0) {
                continue;
            }

            if (glitch == 1) {
                // Shift left 1
                for (int innerLoop = 1; innerLoop < 8; innerLoop++) {
                    tempLEDs[loop * 8 + innerLoop - 1] = tempLEDs[loop * 8 + innerLoop];
                }
            }

            if (glitch == 2) {
                // Shift left 2
                for (int innerLoop = 2; innerLoop < 8; innerLoop++) {
                    tempLEDs[loop * 8 + innerLoop - 2] = tempLEDs[loop * 8 + innerLoop];
                }
            }

            if (glitch == 3) {
                // Shift right 1
                for (int innerLoop = 6; innerLoop > 0; innerLoop--) {
                    tempLEDs[loop * 8 + innerLoop + 1] = tempLEDs[loop * 8 + innerLoop];
                }
            }

            if (glitch == 4) {
                // Shift right 2
                for (int innerLoop = 5; innerLoop > 0; innerLoop--) {
                    tempLEDs[loop * 8 + innerLoop + 2] = tempLEDs[loop * 8 + innerLoop];
                }
            }
        }

        temp = new SenseHatLEDImage(tempLEDs);
        temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.NORMAL);

        return temp;
    }
}
