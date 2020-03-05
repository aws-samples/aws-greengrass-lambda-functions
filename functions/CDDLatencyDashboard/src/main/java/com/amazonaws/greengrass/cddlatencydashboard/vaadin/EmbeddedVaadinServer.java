package com.amazonaws.greengrass.cddlatencydashboard.vaadin;

import com.amazonaws.greengrass.cddlatencydashboard.App;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.startup.ApplicationRouteRegistry;
import com.vaadin.flow.server.startup.ServletContextListeners;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class EmbeddedVaadinServer implements GreengrassStartEventHandler {
    private static final Logger log = LoggerFactory.getLogger(EmbeddedVaadinServer.class);

    @Inject
    Set<Class<? extends Component>> vaadinComponents;
    @Inject
    NativeProcessHelper nativeProcessHelper;

    @Inject
    public EmbeddedVaadinServer() {
    }

    public static void main(String[] args) {
        // For debugging
        EmbeddedVaadinServer embeddedVaadinServer = App.appInjector.embeddedVaadin();
        embeddedVaadinServer.start();
    }

    public void start() {
        Thread serverThread = new Thread(this::innerStart);

        serverThread.start();
    }

    private static boolean isProductionMode() {
        final String probe = "META-INF/maven/com.vaadin/flow-server-production-mode/pom.xml";
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(probe) != null;
    }

    private static Resource findWebRoot() throws MalformedURLException {
        // Don't look up directory as a resource, it's unreliable: https://github.com/eclipse/jetty.project/issues/4173#issuecomment-539769734
        // Instead we'll look up the /webapp/ROOT and retrieve the parent folder from that.
        final URL f = EmbeddedVaadinServer.class.getResource("/webapp/ROOT");
        if (f == null) {
            throw new IllegalStateException("Invalid state: the resource /webapp/ROOT doesn't exist, has webapp been packaged in as a resource?");
        }
        final String url = f.toString();
        if (!url.endsWith("/ROOT")) {
            throw new RuntimeException("Parameter url: invalid value " + url + ": doesn't end with /ROOT");
        }

        // Resolve file to directory
        URL webRoot = new URL(url.substring(0, url.length() - 5));
        log.warn("/webapp/ROOT is " + f);
        log.warn("WebRoot is " + webRoot);
        return Resource.newResource(webRoot);
    }

    private void innerStart() {
        synchronized (EmbeddedVaadinServer.class) {
            try {
                tryToSetProductionMode();

                WebAppContext context = new WebAppContext();
                context.setBaseResource(findWebRoot());
                context.setContextPath("/");
                context.addServlet(VaadinServlet.class, "/*");
                context.getServletContext().setExtendedListenerTypes(true);
                // Required or no routes are registered
                context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
                context.addEventListener(new ServletContextListeners());

                Server server = new Server(8001);

                server.setHandler(context);

                ApplicationRouteRegistry applicationRouteRegistry = ApplicationRouteRegistry.getInstance(context.getServletContext());
                vaadinComponents.forEach(componentClass -> autoWire(applicationRouteRegistry, componentClass));

                server.start();
                server.join();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void autoWire(ApplicationRouteRegistry applicationRouteRegistry, Class<? extends Component> componentClass) {
        applicationRouteRegistry.setRoute(getRoute(componentClass), LatencyDashboardView.class, new ArrayList<>());
    }

    private void tryToSetProductionMode() {
        if (isProductionMode()) {
            // fixes https://github.com/mvysny/vaadin14-embedded-jetty/issues/1
            log.warn("Production mode detected, enforcing");
            System.setProperty("vaadin.productionMode", "true");
        } else {
            log.warn("NOT production mode");
        }
    }

    public String getRoute(Class<? extends Component> clazz) {
        Route route = clazz.getAnnotation(Route.class);

        if (route == null) {
            return "";
        }

        String routeValue = route.value();

        if (routeValue.equals(Route.NAMING_CONVENTION)) {
            return "";
        }

        return routeValue;
    }

    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        log.info("Greengrass start event received, starting Vaadin server");
        start();
    }
}
