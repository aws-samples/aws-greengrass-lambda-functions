package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.App;
import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;

public class DaggerUIProvider extends DefaultUIProvider {
    @Override
    public UI createInstance(UICreateEvent event) {
        // Create the instance without DI
        UI ui = super.createInstance(event);

        // Is this a DaggerUI that needs injection?
        if (ui instanceof DaggerUI) {
            // Yes, inject the necessary fields
            App.appInjector.inject((DaggerUI) ui);
        }

        return ui;
    }
}
