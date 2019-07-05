# ARM32 Sample C

## What is this function?

This function is the Hello World example from the [AWS Greengrass Core C SDK](https://github.com/aws/aws-greengrass-core-sdk-c). It also contains the rest of the C examples from that repo so it can be reconfigured to run any of those functions.

## Build times

The build script in this directory uses Docker to build the AWS C++ SDK and the example functions. Even on a fast machine this may take hours the first time the build runs.

## Compatibility

Some systems do not run the Resin.io cross-build container properly. Docker will print an error message like this when cross-builds are not possible:

```
standard_init_linux.go:207: exec user process caused "exec format error"
```

If the Docker host is ARM32 it should work without an issue.

## What does the output look like?

If this function is launched successfully then you should expect to see the message `hello world!` on the `hello/world` topic every few seconds.
