package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.ImmutableMessageFromCloudEvent;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.MessageFromCloudEvent;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.TimerFiredEvent;
import com.google.gson.Gson;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push(transport = Transport.LONG_POLLING)
@Theme("valo")
public class SkeletonUI extends DaggerUI {
    public static final String PATTERN = "/*";
    private final Grid<String> leftGrid = new Grid<>();
    private final Grid<String> rightGrid = new Grid<>();
    private final HorizontalLayout horizontalLayout = new HorizontalLayout();

    private final VerticalLayout verticalLayout = new VerticalLayout();

    private final Label gcCountLabel = new Label();
    private final Label gcTimeLabel = new Label();

    private final List<String> leftList = new ArrayList<>();
    private final List<String> rightList = new ArrayList<>();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // Make sure we get events from the dispatcher
        dispatcher.add(ImmutableMessageFromCloudEvent.class, this::messageFromCloud);
        dispatcher.add(TimerFiredEvent.class, this::timerFiredEvent);

        leftGrid.setWidth("100%");
        leftGrid.setCaption("Left grid");
        leftGrid.addColumn(string -> string).setMinimumWidthFromContent(true).setCaption("Event");

        rightGrid.setWidth("100%");
        rightGrid.setCaption("Right grid");
        rightGrid.addColumn(string -> string).setMinimumWidthFromContent(true).setCaption("Event");

        horizontalLayout.addComponentsAndExpand(leftGrid, rightGrid);
        verticalLayout.addComponents(horizontalLayout, gcCountLabel, gcTimeLabel);

        setContent(verticalLayout);
    }

    public void messageFromCloud(MessageFromCloudEvent messageFromCloudEvent) {
        try {
            access(() -> {
                String json = new Gson().toJson(messageFromCloudEvent);
                leftList.add(json);
                leftGrid.setItems(leftList);
                rightList.add(json);
                rightGrid.setItems(rightList);
            });

            updateGcInfo();
        } catch (UIDetachedException e) {
            // Client probably disconnected, ignore
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
        try {
            access(() -> {
                leftList.add(Instant.now().toString());
                leftGrid.setItems(leftList);
                rightList.add(Instant.now().toString());
                rightGrid.setItems(rightList);
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

    @WebServlet(asyncSupported = true)
    @VaadinServletConfiguration(ui = SkeletonUI.class, productionMode = false)
    public static class SkeletonUIServlet extends VaadinDaggerServlet {
    }
}
