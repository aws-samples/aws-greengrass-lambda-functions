package com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces;

import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;

public interface AnimationRunner {
    void startAnimation(Animation animation);

    void stopAnimation();
}
