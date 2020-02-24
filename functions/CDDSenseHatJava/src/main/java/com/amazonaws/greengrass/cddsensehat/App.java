package com.amazonaws.greengrass.cddsensehat;

import com.awslabs.aws.iot.greengrass.cdd.BaselineApp;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class App implements BaselineApp {
    private static final AppInjector appInjector = DaggerAppInjector.create();

    static {
        new App().initialize();
    }

    // Greengrass requires a no-args constructor, do not remove!
    public App() {
    }

    @Override
    public BaselineInjector getBaselineInjector() {
        return appInjector;
    }

    @Override
    public Set<GreengrassStartEventHandler> getStartupHandlers() {
        return new HashSet<>(Arrays.asList(appInjector.startupHandler()));
    }

    @Override
    public Set<GreengrassLambdaEventHandler> getLambdaHandlers() {
        return new HashSet<>(Arrays.asList(appInjector.cddSenseHatListEventHandler(),
                appInjector.cddSenseHatStartEventHandler(),
                appInjector.cddSenseHatStopEventHandler()));
    }
}

