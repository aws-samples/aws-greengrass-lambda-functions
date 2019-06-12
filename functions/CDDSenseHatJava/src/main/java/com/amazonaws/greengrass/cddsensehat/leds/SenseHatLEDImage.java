package com.amazonaws.greengrass.cddsensehat.leds;

public class SenseHatLEDImage {
    private static final int COUNT = 64;
    private static final int CHARACTERS_PER_PIXEL = 2;
    private SenseHatLED[] senseHatLEDs;
    private String framebufferData;
    private String renderedLEDs = null;
    private SenseHatLEDOrientation senseHatLEDOrientation = SenseHatLEDOrientation.NORMAL;

    public SenseHatLEDImage() {
        this.framebufferData = new String(new char[COUNT * CHARACTERS_PER_PIXEL]);
    }

    public SenseHatLEDImage(SenseHatLED[] senseHatLEDs) {
        this.senseHatLEDs = senseHatLEDs;
    }

    public SenseHatLEDImage(SenseHatLED[][] senseHatLEDs) {
        this.senseHatLEDs = new SenseHatLED[64];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                this.senseHatLEDs[x + (y * 8)] = senseHatLEDs[y][x];
            }
        }
    }


    public SenseHatLEDImage(String framebufferData) {
        this.framebufferData = framebufferData;
    }

    public SenseHatLEDOrientation getSenseHatLEDOrientation() {
        return senseHatLEDOrientation;
    }

    public void setSenseHatLEDOrientation(SenseHatLEDOrientation senseHatLEDOrientation) {
        this.senseHatLEDOrientation = senseHatLEDOrientation;
        renderedLEDs = null;
    }

    public String renderToString() {
        if (renderedLEDs != null) {
            return renderedLEDs;
        }

        StringBuilder stringBuilder = new StringBuilder();

        SenseHatLED[] temp = renderToLEDs();

        for (SenseHatLED senseHatLED : temp) {
            stringBuilder.append(senseHatLED.getValue());
        }

        renderedLEDs = stringBuilder.toString();

        return renderedLEDs;
    }

    public SenseHatLED[] renderToLEDs() {
        SenseHatLED[] temp = getSenseHatLEDs();

        if (senseHatLEDOrientation.equals(SenseHatLEDOrientation.CCW_90)) {
            // Do nothing
        } else if (senseHatLEDOrientation.equals(SenseHatLEDOrientation.CW_90)) {
            SenseHatLED[] rotated = new SenseHatLED[COUNT];

            for (int loop = 0; loop < COUNT; loop++) {
                rotated[loop] = temp[COUNT - loop - 1];
            }

            temp = rotated;
        } else if (senseHatLEDOrientation.equals(SenseHatLEDOrientation.UPSIDE_DOWN)) {
            SenseHatLED[] rotated = new SenseHatLED[COUNT];

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    rotated[((8 - x - 1) * 8) + y] = temp[(y * 8) + x];
                }
            }

            temp = rotated;
        } else if (senseHatLEDOrientation.equals(SenseHatLEDOrientation.NORMAL)) {
            SenseHatLED[] rotated = new SenseHatLED[COUNT];

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    rotated[(x * 8) + (8 - y - 1)] = temp[(y * 8) + x];
                }
            }

            temp = rotated;
        }
        return temp;
    }

    public SenseHatLED[] getSenseHatLEDs() {
        if (senseHatLEDs != null) {
            return senseHatLEDs;
        }

        if (framebufferData == null) {
            throw new UnsupportedOperationException("No data");
        }

        SenseHatLED[] output = new SenseHatLED[COUNT];

        int pixelsInFramebuffer = framebufferData.length() / 2;

        for (int position = 0; position < pixelsInFramebuffer; position++) {
            byte first = (byte) framebufferData.charAt(position * 2);
            byte second = (byte) framebufferData.charAt((position * 2) + 1);

            output[position] = new SenseHatLED(first, second);
        }

        for (int position = pixelsInFramebuffer; position < COUNT; position++) {
            output[position] = new SenseHatLED(0, 0, 0);
        }

        return output;
    }
}
