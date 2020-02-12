package com.amazonaws.greengrass.cddembeddedvaadinskeleton;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.StartupHandler;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin.DaggerUI;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin.VaadinDaggerModule;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.vaadin.server.UIProvider;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, VaadinDaggerModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    InputHandler inputHandler();

    UIProvider uiProvider();

    void inject(DaggerUI ui);
}
