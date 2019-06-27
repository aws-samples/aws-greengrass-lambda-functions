# DockerPython

## What is this function?

DockerPython presents way to manage docker containers.
This lambda uses an alternative model to the CDDDockerJava way of managing containers.

Instead of instructing the lambda to pull containers over MQTT, the DockerPython lambda has information about the containers it should pull hard-coded into it. 
This means that every lambda deployment roughly corresponds to every container deployment. 
This model fits better with some use cases by avoiding complications from missing or repeated MQTT messages. 
Additionally, this model allows you to specify optional arguments to pass to _docker run_ through the Docker Python SDK, such as specifying the path to a camera device.

By submitting this pull request, I confirm that you can use, modify, copy, and redistribute this contribution, under the terms of your choice.

## Setup

1. Clone this repository.
2. Run the following command
```
GGP -g groupname -a architecture -d deployments/python-docker.conf --script
```
where GGP is your provisioner method of choice (docker or jar), 
groupname is the name of the group you'd like to create,
and architecture is the architecture of your core device, either X86_64 or ARM32 or ARM64.

> NOTE: you must run docker containers that are compatible with your architecture. The default containers in DockerPython.py are X86_64. If you need to change this, modify MY_IMAGES in the python script to use the image name "armhf/hello-world"

3. Copy the generated script located in ./build to your core device and run it.
4. In the AWS console, subscribe to the topics NAME_OF_CORE/docker/logs and NAME_OF_CORE/docker/info. Note that the name of the core is the name of the group concatenated with "_Core"
5. In the /docker/info topic you'll see diagnostic messages from the lambda, in /docker/logs you'll see logs read directly from the standard output of the containers. These logs have been forwarded over MQTT by the lambda.
6. If you'd like, modify MY_IMAGES with additional options and redeploy with
```
GGP -g groupname -a architecture -d deployments/python-docker.conf
```