package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.App;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.timmattison.embeddedvaadin.vaadin.AbstractDaggerEmbeddedVaadinServer;
import com.vaadin.flow.component.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class EmbeddedVaadinServer extends AbstractDaggerEmbeddedVaadinServer implements GreengrassStartEventHandler {
    private static final Logger log = LoggerFactory.getLogger(EmbeddedVaadinServer.class);

    @Inject
    Set<Class<? extends Component>> vaadinComponents;

    @Inject
    public EmbeddedVaadinServer() {
    }

    public static void main(String[] args) {
        // For debugging
        EmbeddedVaadinServer embeddedVaadinServer = App.appInjector.embeddedVaadin();
        embeddedVaadinServer.start();
    }

    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        log.info("Greengrass start event received, starting Vaadin server");
        start();
    }
}
