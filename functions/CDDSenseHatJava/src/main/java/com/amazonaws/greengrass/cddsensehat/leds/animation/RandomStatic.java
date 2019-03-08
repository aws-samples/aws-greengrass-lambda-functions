package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.Random;

/**
 * Created by timmatt on 3/6/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RandomStatic implements Animation {
    private final Random random = new Random();
    @Getter
    long period = 20;

    @Override
    public SenseHatLEDImage nextImage() {
        SenseHatLED[] senseHatLEDs = new SenseHatLED[64];

        for (int loop = 0; loop < 64; loop++) {
            int intensity = random.nextBoolean() ? 255 : 0;
            senseHatLEDs[loop] = new SenseHatLED(intensity, intensity, intensity);
        }

        return new SenseHatLEDImage(senseHatLEDs);
    }
}
