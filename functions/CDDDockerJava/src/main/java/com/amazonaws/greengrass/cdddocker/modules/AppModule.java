package com.amazonaws.greengrass.cdddocker.modules;

import com.amazonaws.greengrass.cdddocker.docker.BasicDockerHelper;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.amazonaws.greengrass.cdddocker.handlers.*;
import com.amazonaws.greengrass.cdddocker.providers.AmazonECRClientProvider;
import com.amazonaws.services.ecr.AmazonECRClient;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        // Any class that needs to receive messages must be set up as an eager singleton so Guice will autowire them at startup

        // StartupHandler will get a message indicating that the core has started
        bind(StartupHandler.class).asEagerSingleton();

        // RequestHandler will only call its execute method on messages that are from the Topics.inputTopic topic
        bind(RequestHandler.class).asEagerSingleton();

        // Docker helper
        bind(DockerHelper.class).to(BasicDockerHelper.class);

        // Docker event handlers
        bind(DockerListRequestHandler.class).asEagerSingleton();
        bind(DockerPullRequestHandler.class).asEagerSingleton();
        bind(DockerRunRequestHandler.class).asEagerSingleton();
        bind(DockerStopRequestHandler.class).asEagerSingleton();

        // This will use the default credential provider chain which should use TES and get temporary credentials for the Greengrass Core's role
        bind(AmazonECRClient.class).toProvider(AmazonECRClientProvider.class);

        bind(LoggingHelper.class).to(BasicLoggingHelper.class);
    }
}
