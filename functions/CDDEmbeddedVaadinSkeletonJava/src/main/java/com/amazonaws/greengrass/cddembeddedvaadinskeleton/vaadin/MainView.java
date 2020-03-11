package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.App;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.ImmutableMessageFromCloudEvent;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.TimerFiredEvent;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.google.gson.Gson;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Route
@Push
@PreserveOnRefresh
public class MainView extends Composite<VerticalLayout> {
    private final Logger log = LoggerFactory.getLogger(MainView.class);

    private final Grid<String> leftGrid = new Grid<>();
    private final Grid<String> rightGrid = new Grid<>();
    private final HorizontalLayout horizontalLayout = new HorizontalLayout();

    private final VerticalLayout verticalLayout = new VerticalLayout();

    private final Label gcCountLabel = new Label();
    private final Label gcTimeLabel = new Label();
    private final Label leftItemCountLabel = new Label();
    private final Label rightItemCountLabel = new Label();

    private final List<String> leftList = new ArrayList<>();
    private final List<String> rightList = new ArrayList<>();
    private final List<Consumer> consumersToRemove = new ArrayList<>();

    @Inject
    Dispatcher dispatcher;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Android-style post constructor injection
        App.appInjector.inject(this);

        log.warn("Attached! " + attachEvent.getUI().getUIId());
        consumersToRemove.add(dispatcher.add(ImmutableMessageFromCloudEvent.class, this::messageFromCloud));
        consumersToRemove.add(dispatcher.add(TimerFiredEvent.class, this::timerFiredEvent));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        log.warn("Detached! " + detachEvent.getUI().getUIId());
        super.onDetach(detachEvent);
        consumersToRemove.forEach(dispatcher::remove);
    }

    public MainView() {
        leftGrid.setWidth("100%");
        leftGrid.addColumn(string -> string).setAutoWidth(true).setHeader("Event");

        rightGrid.setWidth("100%");
        rightGrid.addColumn(string -> string).setAutoWidth(true).setHeader("Event");

        horizontalLayout.addAndExpand(leftGrid, rightGrid);
        verticalLayout.add(horizontalLayout, leftItemCountLabel, rightItemCountLabel, gcCountLabel, gcTimeLabel);

        leftGrid.setItems(leftList);
        rightGrid.setItems(rightList);

        getContent().add(verticalLayout);
    }

    public void messageFromCloud(ImmutableMessageFromCloudEvent immutableMessageFromCloudEvent) {
        synchronized (MainView.class) {
            try {
                getUI().ifPresent(ui -> ui.access(() -> {
                    String json = new Gson().toJson(immutableMessageFromCloudEvent);
                    leftList.add(json);
                    rightList.add(json);
                    updateGrids();
                }));

                updateGcInfo();
            } catch (UIDetachedException e) {
                // Client probably disconnected, ignore
            } catch (Exception e) {
                // Do not throw exceptions in event bus subscriber methods
            }
        }
    }

    private void updateGrids() {
        leftGrid.getDataProvider().refreshAll();
        rightGrid.getDataProvider().refreshAll();
        leftGrid.scrollToEnd();
        rightGrid.scrollToEnd();
        leftItemCountLabel.setText(leftList.size() + " item(s) in the left grid");
        rightItemCountLabel.setText(rightList.size() + " item(s) in the right grid");
    }

    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
        synchronized (MainView.class) {
            try {
                getUI().ifPresent(ui -> ui.access(() -> {
                    leftList.add(Instant.now().toString());
                    rightList.add(Instant.now().toString());
                    updateGrids();
                }));

                updateGcInfo();
            } catch (UIDetachedException e) {
                // Client probably disconnected, ignore
            } catch (Exception e) {
                // Do not throw exceptions in event bus subscriber methods
            }
        }
    }

    public void updateGcInfo() {
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

        getUI().ifPresent(ui -> ui.access(() -> {
            gcCountLabel.setText("GC count: " + finalTotalGcCount);
            gcTimeLabel.setText("GC time: " + finalTotalGcTime);
        }));
    }
}
