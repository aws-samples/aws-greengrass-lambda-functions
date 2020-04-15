package com.amazonaws.greengrass.cddmdnsserviceresolver.mdns;

import com.amazonaws.greengrass.cddmdnsserviceresolver.data.ImmutableServiceEvent;
import com.amazonaws.greengrass.cddmdnsserviceresolver.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.helpers.JsonHelper;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.net.InetAddress;
import java.util.Optional;

public class MdnsServiceListener implements ServiceListener {
    private final Logger log = LoggerFactory.getLogger(MdnsServiceListener.class);

    @Inject
    Dispatcher dispatcher;
    @Inject
    Topics topics;
    @Inject
    JsonHelper jsonHelper;

    @Inject
    public MdnsServiceListener() {
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        ImmutableServiceEvent immutableServiceEvent = serviceInfoToImmutableServiceEvent(event.getInfo());
        log.info("Service added: " + immutableServiceEvent);
        dispatcher.publishObjectEvent(topics.getServiceAddedTopic(), immutableServiceEvent);
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        ImmutableServiceEvent immutableServiceEvent = serviceInfoToImmutableServiceEvent(event.getInfo());
        log.info("Service removed: " + immutableServiceEvent);
        dispatcher.publishObjectEvent(topics.getServiceRemovedTopic(), immutableServiceEvent);
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        ImmutableServiceEvent immutableServiceEvent = serviceInfoToImmutableServiceEvent(event.getInfo());
        log.info("Service resolved: " + immutableServiceEvent);
        dispatcher.publishObjectEvent(topics.getServiceResolvedTopic(), immutableServiceEvent);
    }

    private ImmutableServiceEvent serviceInfoToImmutableServiceEvent(ServiceInfo serviceInfo) {
        Optional<List<InetAddress>> optionalInetAddresses = Optional.ofNullable(serviceInfo.getInetAddresses())
                .map(List::of);

        // Use an empty port value instead of zero
        Optional<Integer> optionalPort = Optional.ofNullable(serviceInfo.getPort() == 0 ? null : serviceInfo.getPort());

        return ImmutableServiceEvent.builder()
                .inetAddresses(optionalInetAddresses)
                .port(optionalPort)
                .domain(serviceInfo.getDomain())
                .protocol(serviceInfo.getProtocol())
                .application(serviceInfo.getApplication())
                .name(serviceInfo.getName())
                .build();
    }
}
