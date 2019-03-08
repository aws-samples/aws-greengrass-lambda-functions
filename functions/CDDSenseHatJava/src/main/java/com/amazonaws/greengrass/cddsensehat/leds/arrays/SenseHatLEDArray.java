package com.amazonaws.greengrass.cddsensehat.leds.arrays;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;

import java.io.IOException;

/**
 * Created by timmatt on 2/20/17.
 */
public interface SenseHatLEDArray {
    SenseHatLEDImage get() throws IOException;

    void put(SenseHatLEDImage senseHatLEDImage) throws IOException;
}
