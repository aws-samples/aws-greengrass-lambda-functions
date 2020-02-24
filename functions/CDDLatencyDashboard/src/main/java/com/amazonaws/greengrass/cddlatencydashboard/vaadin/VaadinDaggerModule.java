package com.amazonaws.greengrass.cddlatencydashboard.vaadin;

import com.vaadin.server.UIProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class VaadinDaggerModule {
    @Provides
    public UIProvider getUIProvider() {
        return new DaggerUIProvider();
    }
}
