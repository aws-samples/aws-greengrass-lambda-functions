package com.amazonaws.greengrass.cddsensehat;

import com.amazonaws.greengrass.cddsensehat.leds.LinuxOutputStreamWriterFactory;
import com.amazonaws.greengrass.cddsensehat.leds.OutputStreamWriterFactory;
import com.amazonaws.greengrass.cddsensehat.leds.animation.*;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.BasicAnimationRunner;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.amazonaws.greengrass.cddsensehat.leds.arrays.LinuxSenseHatLEDArray;
import com.amazonaws.greengrass.cddsensehat.leds.arrays.SenseHatLEDArray;
import com.amazonaws.greengrass.cddsensehat.leds.characters.BasicCharacters;
import com.amazonaws.greengrass.cddsensehat.leds.characters.Characters;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Module
public class AppModule {
    @Provides
    @Singleton
    public AnimationRunner provideAnimationRunner(BasicAnimationRunner basicAnimationRunner) {
        return basicAnimationRunner;
    }

    @Provides
    public OutputStreamWriterFactory provideOutputStreamWriterFactory(LinuxOutputStreamWriterFactory linuxOutputStreamWriterFactory) {
        return linuxOutputStreamWriterFactory;
    }

    @Provides
    public Characters provideCharacters(BasicCharacters basicCharacters) {
        return basicCharacters;
    }

    @Provides
    @Singleton
    public SenseHatLEDArray provideSenseHatLEDArray(LinuxSenseHatLEDArray linuxSenseHatLEDArray) {
        return linuxSenseHatLEDArray;
    }

    @Provides
    @ElementsIntoSet
    public Set<Animation> provideAnimations(Blank blank,
                                            Counter counter,
                                            DisplayNumber displayNumber,
                                            Fire fire,
                                            GlitchNumber glitchNumber,
                                            RandomStatic randomStatic,
                                            SpinNumber spinNumber) {
        return new HashSet<>(Arrays.asList(blank,
                counter,
                displayNumber,
                fire,
                glitchNumber,
                randomStatic,
                spinNumber));
    }
}
