# X86_64 Sample C

## What is this function?

This function is the Hello World example from the [AWS Greengrass Core C SDK](https://github.com/aws/aws-greengrass-core-sdk-c). It also contains the rest of the C examples from that repo so it can be reconfigured to run any of those functions.

## Build times

The build script in this directory uses Docker to build the AWS C++ SDK and the example functions. Even on a fast machine this may take hours the first time the build runs with Dockerfile.full. The normal Dockerfile pulls a pre-built image from Dockerhub.

## Compatibility

If the Docker host is not X86_64 the build script will not work.

## What does the output look like?

If this function is launched successfully then you should expect to see the message `hello world!` on the `hello/world` topic every few seconds.
