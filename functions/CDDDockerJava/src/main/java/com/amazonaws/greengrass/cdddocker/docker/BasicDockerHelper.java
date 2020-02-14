package com.amazonaws.greengrass.cdddocker.docker;

import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.handlers.LoggingHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.PullImageResultCallback;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class BasicDockerHelper implements DockerHelper {
    @Inject
    Topics topics;
    @Inject
    LoggingHelper loggingHelper;
    @Inject
    CombinedDockerClientProvider combinedDockerClientProvider;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public BasicDockerHelper() {
    }

    @Override
    public Optional<Image> getImageFromTag(String tag) {
        List<Image> images = listImages();

        // Is it a repo tag?
        Optional<Image> optionalImage = images.stream()
                .filter(image -> image.getRepoTags() != null)
                .filter(image -> Arrays.stream(image.getRepoTags()).anyMatch(s -> s.equals(tag)))
                .findFirst();

        if (optionalImage.isPresent()) {
            dispatcher.publishMessageEvent(topics.getResponseTopic(), "Found tag [" + tag + "] with ID [" + optionalImage.get().getId() + "]");
            return optionalImage;
        }

        dispatcher.publishMessageEvent(topics.getResponseTopic(), "Tag [" + tag + "] not found");
        return Optional.empty();
    }

    @Override
    public Optional<String> createContainer(String name) {
        Optional<Image> optionalImage = getImageFromTag(name);

        if (!optionalImage.isPresent()) {
            return Optional.empty();
        }

        Image image = optionalImage.get();

        DockerClient dockerClient = combinedDockerClientProvider.getLocalDockerClient();

        CreateContainerResponse container = dockerClient.createContainerCmd(image.getId())
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withCapAdd(Capability.ALL)
                                .withPrivileged(true))
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        return Optional.of(container.getId());
    }

    @Override
    public Optional<Container> getContainerFromImageId(String imageId) {
        List<Container> containers = listContainers();

        Optional<Container> optionalContainer = containers.stream()
                .filter(container -> container.getImage().equals(imageId))
                .findFirst();

        if (optionalContainer.isPresent()) {
            dispatcher.publishMessageEvent(topics.getResponseTopic(), "Found container [" + optionalContainer.get().getId() + "] with image ID [" + imageId + "]");
            return optionalContainer;
        }

        dispatcher.publishMessageEvent(topics.getResponseTopic(), "No container running image [" + imageId + "] found");
        return Optional.empty();
    }

    private List<Container> listContainers() {
        return combinedDockerClientProvider.getLocalDockerClient().listContainersCmd().exec();
    }

    @Override
    public void dumpImagesInfo() {
        List<Image> images = listImages();

        List<Map<String, Optional<Object>>> imageList = images.stream()
                .map(this::convertImageToAttributeMap)
                .collect(Collectors.toList());

        Map<String, List> output = new HashMap<>();
        output.put("images", imageList);

        dispatcher.publishObjectEvent(topics.getResponseTopic(), output);
    }

    private List<Image> listImages() {
        return combinedDockerClientProvider.getLocalDockerClient().listImagesCmd().exec();
    }

    private Map<String, Optional<Object>> convertImageToAttributeMap(Image image) {
        Map<String, Optional<Object>> attributeMap = new HashMap<>();

        attributeMap.put("id", Optional.ofNullable(image.getId()));
        attributeMap.put("names", Optional.ofNullable(image.getRepoTags()));

        return attributeMap;
    }

    @Override
    public void dumpContainersInfo() {
        DockerClient dockerClient = combinedDockerClientProvider.getLocalDockerClient();

        List<Image> images = dockerClient.listImagesCmd().exec();
        List<Container> containers = dockerClient.listContainersCmd().exec();

        List<Map<String, Optional<Object>>> containerList = containers.stream()
                .map(container -> convertContainerToAttributeMap(container, images))
                .collect(Collectors.toList());

        Map<String, List> output = new HashMap<>();
        output.put("containers", containerList);

        dispatcher.publishObjectEvent(topics.getResponseTopic(), output);
    }

    private Map<String, Optional<Object>> convertContainerToAttributeMap(Container container, List<Image> images) {
        Map<String, Optional<Object>> attributeMap = new HashMap<>();

        attributeMap.put("id", Optional.ofNullable(container.getId()));
        attributeMap.put("names", Optional.ofNullable(container.getNames()));
        attributeMap.put("image", Optional.ofNullable(container.getImage()));
        attributeMap.put("labels", Optional.ofNullable(container.getLabels()));
        attributeMap.put("status", Optional.ofNullable(container.getStatus()));
        attributeMap.put("command", Optional.ofNullable(container.getCommand()));
        attributeMap.put("imageId", Optional.ofNullable(container.getImageId()));

        Optional<Image> optionalImage = images.stream()
                .filter(image -> image.getId().equals(container.getImage()))
                .findFirst();

        optionalImage.ifPresent(image -> attributeMap.put("repoTags", Optional.ofNullable(image.getRepoTags())));

        return attributeMap;
    }

    @Override
    public Optional<String> stopContainer(String nameOrId) {
        List<Container> containers = listContainers();

        Optional<Container> optionalContainer = containers.stream()
                .filter(container -> container.getId().equals(nameOrId))
                .findFirst();

        if (!optionalContainer.isPresent()) {
            // The value passed may be a name, it isn't a valid container ID
            Optional<Image> optionalImage = getImageFromTag(nameOrId);

            if (!optionalImage.isPresent()) {
                return Optional.empty();
            }

            Image image = optionalImage.get();

            optionalContainer = getContainerFromImageId(image.getId());
        }

        if (!optionalContainer.isPresent()) {
            return Optional.empty();
        }

        Container container = optionalContainer.get();

        combinedDockerClientProvider.getLocalDockerClient().stopContainerCmd(container.getId()).exec();

        return Optional.of(container.getId());
    }

    @Override
    public void pullImage(String name) throws InterruptedException {
        DockerClient dockerClient;

        if (name.contains(".amazonaws.com/") && (name.contains(".dkr.ecr."))) {
            // Looks like an ECR container, get the ECR client
            dockerClient = combinedDockerClientProvider.getEcrDockerClient();
        } else {
            // Use the normal client without ECR auth
            dockerClient = combinedDockerClientProvider.getLocalDockerClient();
        }

        PullImageResultCallback callback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map output = objectMapper.convertValue(item, Map.class);
                // The progress field is an ASCII progress bar that isn't really useful so we always remove it if it is there
                output.remove("progress");
                dispatcher.publishObjectEvent(topics.getResponseTopic(), output);
                super.onNext(item);
            }

            @Override
            public void onError(Throwable throwable) {
                loggingHelper.logAndPublish(Optional.empty(), topics.getResponseTopic(), "Error: " + throwable.getMessage());
                super.onError(throwable);
            }
        };

        dockerClient.pullImageCmd(name)
                .withAuthConfig(dockerClient.authConfig())
                .exec(callback)
                .awaitCompletion();
    }
}
