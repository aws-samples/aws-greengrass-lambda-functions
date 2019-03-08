package com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces;

import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;

/**
 * Created by timmatt on 3/6/17.
 */
public interface AnimationRunner {
    void startAnimation(Animation animation);

    void stopAnimation();
}
