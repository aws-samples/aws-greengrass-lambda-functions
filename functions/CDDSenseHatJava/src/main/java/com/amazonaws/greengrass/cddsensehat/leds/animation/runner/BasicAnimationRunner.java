package com.amazonaws.greengrass.cddsensehat.leds.animation.runner;

import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.amazonaws.greengrass.cddsensehat.leds.arrays.SenseHatLEDArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BasicAnimationRunner implements AnimationRunner {
    private final SenseHatLEDArray senseHatLEDArray;

    private Optional<Timer> timer = Optional.empty();

    @Override
    public void startAnimation(Animation animation) {
        stopAnimation();

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
