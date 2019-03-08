package com.amazonaws.greengrass.cddsensehat.leds.animation;

import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLED;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDOrientation;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.Random;

/**
 * Created by timmatt on 2/21/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Fire implements Animation {
    protected SenseHatLED black = new SenseHatLED(0, 0, 0);
    protected SenseHatLED red = new SenseHatLED(255, 0, 0);
    SenseHatLED[][] senseHatLEDs;
    Random random = new Random(4);

    @Getter
    long period = 10;

    @Override
    public SenseHatLEDImage nextImage() {
        boolean[] generatedLine = new boolean[8];
        SenseHatLED[][] temp = new SenseHatLED[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                temp[y][x] = black;
            }
        }

        if (senseHatLEDs == null) {
            senseHatLEDs = new SenseHatLED[8][8];

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    senseHatLEDs[y][x] = black;
                }
            }
        }

        for (int x = 0; x < 8; x++) {
            generatedLine[x] = random.nextBoolean();
        }

        // Evaluate the inside 6x7 grid
        for (int y = 0; y < 7; y++) {
            for (int x = 1; x < 7; x++) {
                evaluateCell(temp, y, x);
            }
        }

        int count;

        // Evaluate the bottom line
        for (int x = 1; x < 7; x++) {
            count = ((senseHatLEDs[7][x - 1] == red) ? 1 : 0) +
                    ((senseHatLEDs[7][x + 1] == red) ? 1 : 0) +
                    (generatedLine[x - 1] ? 1 : 0) +
                    (generatedLine[x] ? 1 : 0) +
                    (generatedLine[x + 1] ? 1 : 0);

            setIfCountHighEnough(temp, x, 7, count);
        }

        // Bottom line left pixel
        count = (generatedLine[0] ? 1 : 0) +
                (generatedLine[1] ? 1 : 0);

        setIfCountHighEnough(temp, 0, 7, count);

        // Bottom line right pixel
        count = (generatedLine[6] ? 1 : 0) +
                (generatedLine[7] ? 1 : 0);

        setIfCountHighEnough(temp, 7, 7, count);

        // Evaluate the left side
        for (int y = 0; y < 7; y++) {
            count = ((senseHatLEDs[y + 1][0] == red) ? 1 : 0) +
                    ((senseHatLEDs[y + 1][1] == red) ? 1 : 0);
            setIfCountHighEnough(temp, 0, y, count);
        }

        // Evaluate the right side
        for (int y = 0; y < 7; y++) {
            count = ((senseHatLEDs[y + 1][6] == red) ? 1 : 0) +
                    ((senseHatLEDs[y + 1][7] == red) ? 1 : 0);
            setIfCountHighEnough(temp, 7, y, count);
        }

        senseHatLEDs = temp;

        SenseHatLEDImage image = new SenseHatLEDImage(senseHatLEDs);
        image.setSenseHatLEDOrientation(SenseHatLEDOrientation.NORMAL);
        return image;
    }

    private void setIfCountHighEnough(SenseHatLED[][] temp, int x, int y, int count) {
        if (count >= 3) {
            temp[y][x] = red;
            return;
        }

        temp[y][x] = black;
    }

    private void evaluateCell(SenseHatLED[][] temp, int y, int x) {
        int count = ((senseHatLEDs[y][x - 1] == red) ? 1 : 0) +
                ((senseHatLEDs[y][x + 1] == red) ? 1 : 0) +
                ((senseHatLEDs[y + 1][x - 1] == red) ? 1 : 0) +
                ((senseHatLEDs[y + 1][x] == red) ? 1 : 0) +
                ((senseHatLEDs[y + 1][x + 1] == red) ? 1 : 0);

        setIfCountHighEnough(temp, x, y, count);
    }
}
