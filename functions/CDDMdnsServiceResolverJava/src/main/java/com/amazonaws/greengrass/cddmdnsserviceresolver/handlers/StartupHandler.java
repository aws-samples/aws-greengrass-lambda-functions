package com.amazonaws.greengrass.cddmdnsserviceresolver.handlers;

import com.amazonaws.greengrass.cddmdnsserviceresolver.data.Topics;
import com.amazonaws.greengrass.cddmdnsserviceresolver.mdns.MdnsServiceListener;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.helpers.TimerHelper;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jmdns.JmDNS;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class StartupHandler implements GreengrassStartEventHandler {
    public static final String SERVICE_TYPE = "_http._tcp.local.";
    private final Logger log = LoggerFactory.getLogger(StartupHandler.class);
    private static final int START_DELAY_MS = 5000;
    private static final int LOGGING_PERIOD_MS = 60000;
    private static final int INTERFACE_CHECK_PERIOD_MS = 60000;
    @Inject
    Dispatcher dispatcher;
    @Inject
    Topics topics;
    @Inject
    MdnsServiceListener mdnsServiceListener;
    @Inject
    TimerHelper timerHelper;
    private List<JmDNS> jmDNSList = List.empty();

    @Inject
    public StartupHandler() {
    }

    /**
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param immutableGreengrassStartEvent
     */
    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        dispatcher.publishMessageEvent(topics.getLoggingTopic(), "mDNS service resolver started [" + System.nanoTime() + "]");

        Runnable loggingRunnable = () -> dispatcher.publishMessageEvent(topics.getLoggingTopic(), "mDNS service resolver still running... [" + System.nanoTime() + "]");
        timerHelper.scheduleRunnable(loggingRunnable, START_DELAY_MS, LOGGING_PERIOD_MS, TimeUnit.MILLISECONDS);

        // Periodically try to bind new interfaces if any show up
        timerHelper.scheduleRunnable(this::bindNewInterfaces, START_DELAY_MS, INTERFACE_CHECK_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    private void bindNewInterfaces() {
        List<InetAddress> allAddresses = getAllAddresses();
        List<InetAddress> listeningAddresses = getListeningAddresses();
        List<InetAddress> addressesToTry = allAddresses.removeAll(listeningAddresses);

        if (addressesToTry.isEmpty()) {
            log.info("No new interfaces");
            return;
        }

        log.info("New interfaces to listen on:");
        addressesToTry.forEach(address -> log.info(" - " + address));

        log.info("Creating JmDNS instances...");
        List<JmDNS> newListeners = addressesToTry
                // Try to create the JmDNS instances
                .map(inetAddress -> Try.of(() -> JmDNS.create(inetAddress)))
                // Get all of the successful attempts
                .filter(Try::isSuccess)
                .map(Try::get)
                // Store them as a list
                .collect(List.collector());

        // Add the service listener to all of the new jmDNS instances
        newListeners.forEach(jmDNS -> jmDNS.addServiceListener(SERVICE_TYPE, mdnsServiceListener));

        // Add the new listeners to the existing list
        jmDNSList = jmDNSList.appendAll(newListeners);

        // Determine which addresses failed
        List<InetAddress> failedInetAddresses = List.ofAll(allAddresses).removeAll(listeningAddresses);

        // Log all of the failed addresses
        failedInetAddresses.forEach(inetAddress -> log.error("Address [" + inetAddress + "] was unable to start listening for mDNS packets"));
    }

    private List<InetAddress> getListeningAddresses() {
        // Determine which addresses were successful
        return jmDNSList
                .map(jmDNS -> Try.of(jmDNS::getInetAddress).get())
                .collect(List.collector());
    }

    public List<InetAddress> getAllAddresses() {
        // Get a list of all of the network interfaces
        return Try.of(NetworkInterface::getNetworkInterfaces)
                // Convert them to a list and then to a Vavr list
                .map(Collections::list)
                .map(List::ofAll)
                // Use an empty list if there was an exception (e.g. no interfaces)
                .getOrElse(List::empty)
                // Remove the interfaces that are down
                .filter(networkInterface -> Try.of(networkInterface::isUp).getOrElse(false))
                // Get all of the addresses
                .map(NetworkInterface::getInetAddresses)
                // Convert them all to lists
                .map(Collections::list)
                // Flatten them all to a stream
                .flatMap(innerList -> innerList)
                // Convert the final stream to a list
                .collect(List.collector());
    }
}
