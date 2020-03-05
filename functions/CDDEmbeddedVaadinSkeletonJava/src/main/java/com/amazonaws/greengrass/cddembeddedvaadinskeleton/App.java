package com.amazonaws.greengrass.cddembeddedvaadinskeleton;

import com.awslabs.aws.iot.greengrass.cdd.BaselineApp;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class App implements BaselineApp {
    // Vaadin needs this to be accessible publicly later
    public static final AppInjector appInjector = DaggerAppInjector.create();

    static {
        new App().initialize();
    }

    // Greengrass requires a no-args constructor, do not remove
    public App() {
    }

    @Override
    public BaselineInjector getBaselineInjector() {
        return appInjector;
    }

    @Override
    public Set<GreengrassStartEventHandler> getStartupHandlers() {
        return new HashSet<>(Arrays.asList(appInjector.startupHandler(), appInjector.embeddedVaadin()));
    }

    @Override
    public Set<GreengrassLambdaEventHandler> getLambdaHandlers() {
        return new HashSet<>(Arrays.asList(appInjector.inputHandler()));
    }
}
