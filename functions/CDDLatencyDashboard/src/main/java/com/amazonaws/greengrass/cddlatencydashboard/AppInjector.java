package com.amazonaws.greengrass.cddlatencydashboard;

import com.amazonaws.greengrass.cddlatencydashboard.handlers.InputHandler;
import com.amazonaws.greengrass.cddlatencydashboard.handlers.StartupHandler;
import com.amazonaws.greengrass.cddlatencydashboard.vaadin.DaggerUI;
import com.amazonaws.greengrass.cddlatencydashboard.vaadin.VaadinDaggerModule;
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

    void inject(DaggerUI daggerUi);
}
