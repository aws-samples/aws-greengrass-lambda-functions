package com.amazonaws.greengrass.cddembeddedvaadinskeleton.vaadin;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.App;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.ImmutableMessageFromCloudEvent;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.TimerFiredEvent;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.helpers.JsonHelper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.Instant;
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

    private List<String> leftList = List.empty();
    private List<String> rightList = List.empty();
    private List<Consumer> consumersToRemove = List.empty();

    @Inject
    Dispatcher dispatcher;
    @Inject
    JsonHelper jsonHelper;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Android-style post constructor injection
        App.appInjector.inject(this);

        log.warn("Attached! " + attachEvent.getUI().getUIId());
        consumersToRemove = consumersToRemove.append(dispatcher.add(ImmutableMessageFromCloudEvent.class, this::messageFromCloud));
        consumersToRemove = consumersToRemove.append(dispatcher.add(TimerFiredEvent.class, this::timerFiredEvent));
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

        leftGrid.setItems(leftList.asJava());
        rightGrid.setItems(rightList.asJava());

        getContent().add(verticalLayout);
    }

    private void updateGrids(String data) {
        leftList = leftList.append(data);
        rightList = rightList.append(data);
        leftGrid.setItems(leftList.asJava());
        rightGrid.setItems(rightList.asJava());
        leftItemCountLabel.setText(leftList.size() + " item(s) in the left grid");
        rightItemCountLabel.setText(rightList.size() + " item(s) in the right grid");
        updateGcInfo();
    }

    public void messageFromCloud(ImmutableMessageFromCloudEvent immutableMessageFromCloudEvent) {
        // Ignore all exceptions
        Try.run(() -> getUI().ifPresent(ui -> ui.access(() -> updateGrids(jsonHelper.toJson(immutableMessageFromCloudEvent)))));
    }

    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
        // Ignore all exceptions
        Try.run(() -> getUI().ifPresent(ui -> ui.access(() -> updateGrids(Instant.now().toString()))));
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
