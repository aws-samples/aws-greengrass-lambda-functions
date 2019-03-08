package com.amazonaws.greengrass.cddsensehat.vaadin;

import com.timmattison.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EmbeddedVaadin {
    private final EnvironmentProvider environmentProvider;

    public void start() {
        Thread serverThread = new Thread(() -> innerStart());

        serverThread.start();
    }

    private void innerStart() {
        Optional<String> port = environmentProvider.get("PORT");

        if (!port.isPresent()) {
            log.error("PORT not specified in environment variables, can not start web server");
            System.exit(1);
        }

        Server server = new Server(Integer.parseInt(port.get()));

        ServletContextHandler contextHandler
                = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        ServletHolder sh = new ServletHolder(new MyServlet());
        contextHandler.addServlet(sh, SkeletonUI.PATTERN);
        contextHandler.setInitParameter("ui", SkeletonUI.class.getCanonicalName());

        /*
        // Register cdn.virit.in if present
        try {
            Class cls = Class.forName("in.virit.WidgetSet");
            if (cls != null) {
                contextHandler.getSessionHandler().addEventListener((EventListener) cls.newInstance());
            }
        } catch (Exception ex) {
            Logger.getLogger(EmbeddedVaadin.class.getName()).log(Level.SEVERE, null, ex);
        }
        */

        server.setHandler(contextHandler);

        try {
            server.start();
            server.join();

        } catch (Exception ex) {
            Logger.getLogger(EmbeddedVaadin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
