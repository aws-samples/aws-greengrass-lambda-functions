package com.amazonaws.greengrass.cddlatencydashboard.vaadin;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class EmbeddedVaadin {
    private final Logger log = LoggerFactory.getLogger(EmbeddedVaadin.class);

    @Inject
    EnvironmentProvider environmentProvider;

    @Inject
    public EmbeddedVaadin() {
    }

    public void start() {
        Thread serverThread = new Thread(this::innerStart);

        serverThread.start();
    }

    private void innerStart() {
        Optional<String> port = environmentProvider.get("PORT");

        if (!port.isPresent()) {
            log.error("PORT not specified in environment variables, can not start web server");
            System.exit(1);
        }

        Server server = new Server(Integer.parseInt(port.get()));

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        ServletHolder servletHolder = new ServletHolder(LatencyDashboardUI.SkeletonUIServlet.class);
        contextHandler.addServlet(servletHolder, LatencyDashboardUI.PATTERN);

        server.setHandler(contextHandler);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
