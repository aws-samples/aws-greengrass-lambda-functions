package com.amazonaws.greengrass.cddsensehat.leds;

public class SenseHatLEDPartialImage {
    private final SenseHatLED[][] senseHatLEDs;
    private final String renderedLEDs = null;
    private String framebufferData;

    SenseHatLEDPartialImage(SenseHatLED[][] senseHatLEDs) {
        this.senseHatLEDs = senseHatLEDs;
    }

    public SenseHatLEDImage merge(int x, int y, SenseHatLEDImage senseHatLEDImage) {
        SenseHatLED[] theirSenseHatLEDs = senseHatLEDImage.getSenseHatLEDs();
        SenseHatLED[] tempLEDs = new SenseHatLED[theirSenseHatLEDs.length];

        for (int loop = 0; loop < theirSenseHatLEDs.length; loop++) {
            int globalX = loop % 8;
            int globalY = loop / 8;

            tempLEDs[loop] = theirSenseHatLEDs[loop];

            if ((globalX >= x) && (globalY >= y)) {
                int localX = globalX - x;
                int localY = globalY - y;

                if (localY >= senseHatLEDs.length) {
                    continue;
                }

                SenseHatLED[] currentRow = senseHatLEDs[localY];

                if (localX >= currentRow.length) {
                    continue;
                }

                SenseHatLED currentPixel = currentRow[localX];

                if (currentPixel == null) {
                    continue;
                }

                tempLEDs[loop] = currentPixel;
            }
        }

        return new SenseHatLEDImage(tempLEDs);
    }
}
