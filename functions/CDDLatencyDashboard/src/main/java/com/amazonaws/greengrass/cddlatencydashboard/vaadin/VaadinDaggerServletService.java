package com.amazonaws.greengrass.cddlatencydashboard.vaadin;

import com.amazonaws.greengrass.cddlatencydashboard.App;
import com.vaadin.server.*;

public class VaadinDaggerServletService extends VaadinServletService {
    UIProvider uiProvider;

    protected final class SessionListenerImpl implements SessionInitListener, SessionDestroyListener {
        @Override
        public void sessionInit(SessionInitEvent event) {
            // Remove all other UI providers (if any)
            event.getSession().getUIProviders().forEach(event.getSession()::removeUIProvider);

            // Add the Dagger UI provider
            event.getSession().addUIProvider(uiProvider);
        }

        @Override
        public void sessionDestroy(SessionDestroyEvent event) {
            // Do nothing
        }
    }

    public VaadinDaggerServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        super(servlet, deploymentConfiguration);

        uiProvider = App.appInjector.uiProvider();

        SessionListenerImpl sessionListener = new SessionListenerImpl();
        addSessionInitListener(sessionListener);
        addSessionDestroyListener(sessionListener);
    }
}
