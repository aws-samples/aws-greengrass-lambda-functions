package com.amazonaws.greengrass.cddsensehat.leds.arrays;

/*
public class MacOSSenseHatLEDArray implements SenseHatLEDArray {
    private final JavaFXLEDArray javaFXLEDArray;
    private SenseHatLEDImage senseHatLEDImage;

    @Inject
    public MacOSSenseHatLEDArray(JavaFXLEDArray javaFXLEDArray) {
        this.javaFXLEDArray = javaFXLEDArray;
    }

    public SenseHatLEDImage get() {
        return senseHatLEDImage;
    }

    public void put(SenseHatLEDImage senseHatLEDImage) {
        this.senseHatLEDImage = senseHatLEDImage;

        SenseHatLEDImage temp = new SenseHatLEDImage(senseHatLEDImage.getSenseHatLEDs());

        SenseHatLEDOrientation orientation = senseHatLEDImage.getSenseHatLEDOrientation();

        // Adjust orientation for our JavaFX view
        if (orientation.equals(SenseHatLEDOrientation.NORMAL)) {
            temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.CCW_90);
        } else if (orientation.equals(SenseHatLEDOrientation.CW_90)) {
            temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.NORMAL);
        } else if (orientation.equals(SenseHatLEDOrientation.UPSIDE_DOWN)) {
            temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.CW_90);
        } else if (orientation.equals(SenseHatLEDOrientation.CCW_90)) {
            temp.setSenseHatLEDOrientation(SenseHatLEDOrientation.UPSIDE_DOWN);
        }

        javaFXLEDArray.draw(temp);
    }
}
*/
