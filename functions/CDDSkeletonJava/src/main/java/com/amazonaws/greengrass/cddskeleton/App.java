package com.amazonaws.greengrass.cddskeleton;

import com.amazonaws.greengrass.cddskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddskeleton.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.BaselineApp;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

public class App implements BaselineApp {
    private static AppInjector appInjector = DaggerAppInjector.create();
    private static StartupHandler startupHandler = appInjector.startupHandler();
    private static InputHandler inputHandler = appInjector.inputHandler();
    private static Dispatcher dispatcher = appInjector.dispatcher();
    private static EnvironmentProvider environmentProvider = appInjector.environmentProvider();
    private static App app = new App();

    static {
        app.initialize();
    }

    // Greengrass requires a no-args constructor, do not remove!
    public App() {
    }

    @Override
    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public EnvironmentProvider getEnvironmentProvider() {
        return environmentProvider;
    }
}
