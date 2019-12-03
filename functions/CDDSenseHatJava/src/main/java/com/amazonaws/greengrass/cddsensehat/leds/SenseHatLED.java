package com.amazonaws.greengrass.cddsensehat.leds;

public class SenseHatLED {
    private static final int BLUE_SHIFT = 10;
    private static final int RED_SHIFT = 5;
    private static final int MASK_5_BITS = 0x1F;
    private final int red;
    private final int green;
    private final int blue;
    private long binaryValue = -1;
    private char firstCharacterValue;
    private char secondCharacterValue;
    private String stringValue = null;

    public SenseHatLED(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public SenseHatLED(byte first, byte second) {
        long temp = ((first << 8) & 0xFF00) | (second & 0xFF);

        this.blue = (int) ((temp >> BLUE_SHIFT) & MASK_5_BITS);
        this.red = (int) ((temp >> RED_SHIFT) & MASK_5_BITS);
        this.green = (int) (temp & MASK_5_BITS);
    }

    String getValue() {
        if (stringValue == null) {
            binaryValue = 0;
            binaryValue |= ((blue & MASK_5_BITS) << BLUE_SHIFT);
            binaryValue |= ((red & MASK_5_BITS) << RED_SHIFT);
            binaryValue |= (green & MASK_5_BITS);

            firstCharacterValue = (char) ((binaryValue >> 8) & 0xFF);
            secondCharacterValue = (char) (binaryValue & 0xFF);
            stringValue = new String(new char[]{firstCharacterValue, secondCharacterValue});
        }

        return stringValue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SenseHatLED that = (SenseHatLED) o;

        if (red != that.red) return false;
        if (green != that.green) return false;
        return blue == that.blue;
    }

    @Override
    public int hashCode() {
        int result = red;
        result = 31 * result + green;
        result = 31 * result + blue;
        return result;
    }

    @Override
    public String toString() {
        return ((red != 0) || (green != 0) || (blue != 0)) ? "X" : "_";
    }
}
