package com.amazonaws.greengrass.cddlatencydashboard.vaadin;

import com.amazonaws.greengrass.cddlatencydashboard.App;
import com.amazonaws.greengrass.cddlatencydashboard.events.ImmutableMessageFromCloudEvent;
import com.amazonaws.greengrass.cddlatencydashboard.events.Latencies;
import com.amazonaws.greengrass.cddlatencydashboard.events.TimerFiredEvent;
import com.amazonaws.greengrass.cddlatencydashboard.helpers.JsonHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Route
@Push(transport = Transport.LONG_POLLING)
@PreserveOnRefresh
public class LatencyDashboardView extends Composite<VerticalLayout> {
    private final Logger log = LoggerFactory.getLogger(LatencyDashboardView.class);
    private final Label gridLabel = new Label();
    private final Grid<Latencies> latencyGrid = new Grid<>();
    private final List<Latencies> latencyList = new ArrayList<>();
    private final JsonHelper jsonHelper = new JsonHelper();

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

    public LatencyDashboardView() {
        String title = Optional.ofNullable(System.getenv("TITLE")).orElse("Data grid");
        gridLabel.setText(title);
        latencyGrid.setWidth("100%");
        latencyGrid.setItems(latencyList);

        getContent().addAndExpand(new VerticalLayout(gridLabel, latencyGrid));
    }

    public void messageFromCloud(ImmutableMessageFromCloudEvent immutableMessageFromCloudEvent) {
        synchronized (LatencyDashboardView.class) {
            try {
                getUI().ifPresent(ui -> ui.access(() -> {
                    Latencies latencies = jsonHelper.fromJson(Latencies.class, immutableMessageFromCloudEvent.getMessage().getBytes());
                    latencyList.add(latencies);

                    if (latencyGrid.getColumns().size() == 0) {
                        latencyGrid.removeAllColumns();
                        IntStream.range(0, latencies.getLatencies().size())
                                .forEachOrdered(index -> latencyGrid.addColumn(rowValue -> rowValue.getLatencies().get(index).getValue())
                                        .setHeader(latencies.getLatencies().get(index).getName()));
                    }

                    latencyGrid.getDataProvider().refreshAll();
                    latencyGrid.scrollToEnd();
                }));
            } catch (UIDetachedException e) {
                // Client probably disconnected, ignore
            } catch (Exception e) {
                // Do not throw exceptions in event bus subscriber methods
            }
        }
    }

    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
        synchronized (LatencyDashboardView.class) {
            try {
                getUI().ifPresent(ui -> ui.access(() -> {
                    // Do nothing for now
                }));
            } catch (UIDetachedException e) {
                // Client probably disconnected, ignore
            } catch (Exception e) {
                // Do not throw exceptions in event bus subscriber methods
            }
        }
    }
}
