package com.amazonaws.greengrass.cddsensehat.vaadin;

import com.amazonaws.greengrass.cddsensehat.events.TimerFiredEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.guice.annotation.GuiceUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push(transport = Transport.LONG_POLLING)
@Theme("valo")
@GuiceUI
@Slf4j
public class SkeletonUI extends UI {
    public static final String PATTERN = "/*";
    private static final String NAME = "SkeletonUIServlet";
    private static int counter = 0;
    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final Label gcCountLabel = new Label();
    private final Label gcTimeLabel = new Label();
    private final Label messageCountLabel = new Label();
    @Inject
    EventBus eventBus;

    /*
    @Subscribe
    public void messageFromCloud(MessageFromCloudEvent messageFromCloudEvent) {
        try {
            access(() -> {
                String json = new Gson().toJson(messageFromCloudEvent);
                grid.setItems(leftList);
            });

            updateGcInfo();
        } catch (UIDetachedException e) {
            // Client probably disconnected, ignore
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }
    */

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        log.info("In init");
        log.info("Event bus is " + ((eventBus == null) ? "NULL" : "NOT NULL"));
        // Make sure we get events from the event bus
        eventBus.register(this);

        verticalLayout.addComponents(messageCountLabel, gcCountLabel, gcTimeLabel);

        setContent(verticalLayout);
        log.info("Init completed");
    }

    @Subscribe
    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
        try {
            access(() -> {
                messageCountLabel.setValue("Messages: " + counter);
                counter++;
            });

            updateGcInfo();
        } catch (UIDetachedException e) {
            // Client probably disconnected, ignore
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    public void updateGcInfo() {
        synchronized (SkeletonUI.class) {
            long totalGcCount = 0;
            long totalGcTime = 0;

            for (GarbageCollectorMXBean garbageCollectorMXBean : ManagementFactory.getGarbageCollectorMXBeans()) {
                long count = garbageCollectorMXBean.getCollectionCount();

                if (count >= 0) {
                    totalGcCount += count;
                }

                long time = garbageCollectorMXBean.getCollectionTime();

                if (time >= 0) {
                    totalGcTime += time;
                }
            }

            long finalTotalGcCount = totalGcCount;
            long finalTotalGcTime = totalGcTime;

            access(() -> {
                gcCountLabel.setValue("GC count: " + finalTotalGcCount);
                gcTimeLabel.setValue("GC time: " + finalTotalGcTime);
            });
        }
    }

    @WebServlet(urlPatterns = SkeletonUI.PATTERN, name = SkeletonUI.NAME, asyncSupported = true)
    @VaadinServletConfiguration(ui = SkeletonUI.class, productionMode = false)
    public static class SkeletonUIServlet extends VaadinServlet {
    }
}
