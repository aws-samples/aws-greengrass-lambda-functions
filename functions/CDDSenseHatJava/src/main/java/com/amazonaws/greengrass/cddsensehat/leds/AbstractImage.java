package com.amazonaws.greengrass.cddsensehat.leds;

public abstract class AbstractImage {
    protected final SenseHatLED black = new SenseHatLED(0, 0, 0);
    protected final SenseHatLED white = new SenseHatLED(255, 255, 255);
    protected final SenseHatLED red = new SenseHatLED(255, 0, 0);
    protected final SenseHatLED green = new SenseHatLED(0, 255, 0);
    protected final SenseHatLED blue = new SenseHatLED(0, 0, 255);
    private SenseHatLEDPartialImage partialImage;

    public SenseHatLEDPartialImage getPartialImage() {
        if (partialImage != null) {
            return partialImage;
        }

        SenseHatLED[][] temp = new SenseHatLED[getHeight()][getWidth()];

        draw(temp);

        return new SenseHatLEDPartialImage(temp);
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract void draw(SenseHatLED[][] temp);

    protected final void setLine(SenseHatLED[][] temp, int line, SenseHatLED zero, SenseHatLED one, SenseHatLED two, SenseHatLED three) {
        temp[line][0] = zero;
        temp[line][1] = one;
        temp[line][2] = two;
        temp[line][3] = three;
    }

    protected final void setLine(SenseHatLED[][] temp, int line, SenseHatLED zero, SenseHatLED one, SenseHatLED two, SenseHatLED three, SenseHatLED four, SenseHatLED five, SenseHatLED six, SenseHatLED seven) {
        temp[line][0] = zero;
        temp[line][1] = one;
        temp[line][2] = two;
        temp[line][3] = three;
        temp[line][4] = four;
        temp[line][5] = five;
        temp[line][6] = six;
        temp[line][7] = seven;
    }
}
