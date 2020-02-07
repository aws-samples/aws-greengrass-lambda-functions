package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.DockerPullRequest;
import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.google.common.eventbus.Subscribe;
import io.vavr.control.Try;

import javax.inject.Inject;
import java.util.Optional;

public class DockerPullRequestHandler {
    @Inject
    Topics topics;
    @Inject
    LoggingHelper loggingHelper;
    @Inject
    DockerHelper dockerHelper;
    @Inject
    Communication communication;

    @Inject
    public DockerPullRequestHandler() {
    }

    @Subscribe
    public void dockerPullRequest(DockerPullRequest dockerPullRequest) {
        loggingHelper.logAndPublish(Optional.empty(), topics.getResponseTopic(), "Got PULL request for [" + dockerPullRequest.getName() + "]");

        // Note: This must start in a thread so this method returns. Otherwise Greengrass will think the message failed to process.
        new Thread(processDockerPullRequestInThread(dockerPullRequest)).start();
    }

    private Runnable processDockerPullRequestInThread(DockerPullRequest dockerPullRequest) {
        return () -> Try.of(() -> pullImage(dockerPullRequest))
                .recover(Exception.class, this::publishExceptionMessage)
                .get();
    }

    private Void publishExceptionMessage(Exception e) {
        communication.publishMessageEvent(topics.getResponseTopic(), e.getMessage());

        return null;
    }

    private Void pullImage(DockerPullRequest dockerPullRequest) throws InterruptedException {
        dockerHelper.pullImage(dockerPullRequest.getName());

        communication.publishMessageEvent(topics.getResponseTopic(), "Image pulled [" + dockerPullRequest.getName() + "]");

        return null;
    }
}
