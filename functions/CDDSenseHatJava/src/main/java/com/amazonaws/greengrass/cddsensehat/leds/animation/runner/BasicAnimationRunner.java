package com.amazonaws.greengrass.cddsensehat.leds.animation.runner;

import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.amazonaws.greengrass.cddsensehat.leds.arrays.SenseHatLEDArray;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class BasicAnimationRunner implements AnimationRunner {
    @Inject
    SenseHatLEDArray senseHatLEDArray;

    @Inject
    public BasicAnimationRunner() {
    }

    private Optional<Timer> timer = Optional.empty();

    @Override
    public void startAnimation(Animation animation) {
        stopAnimation();

        // Reset the animation
        animation.reset();

        timer = Optional.of(new Timer(true));
        timer.get().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    senseHatLEDArray.put(animation.nextImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, animation.getPeriod() > 0 ? animation.getPeriod() : 1);
    }

    @Override
    public void stopAnimation() {
        timer.ifPresent(Timer::cancel);
        timer = Optional.empty();
    }
}
