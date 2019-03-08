package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

/**
 * Created by timmatt on 3/6/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Blank implements Animation {
    @Getter
    long period = 5000;

    @Override
    public SenseHatLEDImage nextImage() {
        return new SenseHatLEDImage();
    }
}
