package com.amazonaws.greengrass.cddlatencydashboard;

import com.amazonaws.greengrass.cddlatencydashboard.handlers.InputHandler;
import com.amazonaws.greengrass.cddlatencydashboard.handlers.StartupHandler;
import com.amazonaws.greengrass.cddlatencydashboard.vaadin.EmbeddedVaadinModule;
import com.amazonaws.greengrass.cddlatencydashboard.vaadin.EmbeddedVaadinServer;
import com.amazonaws.greengrass.cddlatencydashboard.vaadin.LatencyDashboardView;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, EmbeddedVaadinModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    InputHandler inputHandler();

    EmbeddedVaadinServer embeddedVaadin();

    void inject(LatencyDashboardView latencyDashboardView);
}
