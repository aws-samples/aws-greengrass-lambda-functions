package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.vaadin.ui.UI;

import javax.inject.Inject;

public abstract class DaggerUI extends UI {
    @Inject
    Dispatcher dispatcher;
}
