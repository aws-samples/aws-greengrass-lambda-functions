package com.amazonaws.greengrass.cddsensehat.leds.arrays;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;

import java.io.IOException;

public interface SenseHatLEDArray {
    SenseHatLEDImage get() throws IOException;

    void put(SenseHatLEDImage senseHatLEDImage) throws IOException;
}
