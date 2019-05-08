package com.amazonaws.greengrass.cddsensehat.modules;

import com.amazonaws.greengrass.cddsensehat.App;
import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.handlers.CddSenseHatListEventHandler;
import com.amazonaws.greengrass.cddsensehat.handlers.CddSenseHatStartEventHandler;
import com.amazonaws.greengrass.cddsensehat.handlers.CddSenseHatStopEventHandler;
import com.amazonaws.greengrass.cddsensehat.handlers.StartupHandler;
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
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import javax.inject.Singleton;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CddSenseHatStartEventHandler.class).asEagerSingleton();
        bind(CddSenseHatStopEventHandler.class).asEagerSingleton();
        bind(CddSenseHatListEventHandler.class).asEagerSingleton();
        bind(StartupHandler.class).asEagerSingleton();
        bind(Topics.class);

        bind(AnimationRunner.class).to(BasicAnimationRunner.class).in(Singleton.class);

        bind(OutputStreamWriterFactory.class).to(LinuxOutputStreamWriterFactory.class);
        bind(Characters.class).to(BasicCharacters.class);

        bind(SenseHatLEDArray.class).to(LinuxSenseHatLEDArray.class).in(Singleton.class);

        Multibinder<Animation> animationMultibinder = Multibinder.newSetBinder(binder(), Animation.class);

        animationMultibinder.addBinding().to(Blank.class);
        animationMultibinder.addBinding().to(Counter.class);
        animationMultibinder.addBinding().to(DisplayNumber.class);
        animationMultibinder.addBinding().to(Fire.class);
        animationMultibinder.addBinding().to(GlitchNumber.class);
        animationMultibinder.addBinding().to(RandomStatic.class);
        animationMultibinder.addBinding().to(SpinNumber.class);
    }
}
