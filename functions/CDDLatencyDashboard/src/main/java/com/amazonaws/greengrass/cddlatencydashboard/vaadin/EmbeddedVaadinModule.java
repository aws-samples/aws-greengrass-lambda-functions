package com.amazonaws.greengrass.cddlatencydashboard.vaadin;

import com.vaadin.flow.component.Component;
import dagger.Module;
import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Module
public class EmbeddedVaadinModule {
    @Provides
    @Singleton
    public Set<Class<? extends Component>> provideVaadinComponents() {
        Set<Class<? extends Component>> components = new HashSet<>();
        components.add(LatencyDashboardView.class);

        return components;
    }
}
