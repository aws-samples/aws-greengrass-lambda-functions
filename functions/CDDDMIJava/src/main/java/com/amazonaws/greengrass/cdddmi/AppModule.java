package com.amazonaws.greengrass.cdddmi;

import com.amazonaws.greengrass.cdddmi.dmi.BasicDmiFetcher;
import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    public DmiFetcher provideDmiFetcher(BasicDmiFetcher basicDmiFetcher) {
        return basicDmiFetcher;
    }
}