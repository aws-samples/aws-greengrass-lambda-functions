package com.amazonaws.greengrass.cdddocker.docker;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;

import java.util.Optional;

public interface DockerHelper {
    Optional<Image> getImageFromTag(String tag);

    Optional<String> createContainer(String name);

    Optional<Container> getContainerFromImageId(String imageId);

    void dumpImagesInfo();

    void dumpContainersInfo();

    Optional<String> stopContainer(String name);

    void pullImage(String name) throws InterruptedException;
}
