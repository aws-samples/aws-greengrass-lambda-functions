# DockerPython

## What is this function?
DockerPython presents way to manage docker containers on core devices.
This lambda uses an alternative model to the CDDDockerJava way of managing containers.

## How it works
The DockerPython lambda reads container_config objects from its shadow. These objects have information what docker images to run and with what options.
This model fits better with some use cases by avoiding complications from missing or repeated MQTT messages that could arise with CDDDockerJava's model.
Additionally, this model allows you to specify optional arguments to pass to _docker run_ through the Docker Python SDK, such as specifying the path to a camera device.

## Setup
1. On you GreenGrass Core device, install docker. This setup can vary widely, look up how to install docker for your specific platform.

2. On your GreenGrass Core device, start the docker daemon by running `sudo systemctl start docker` for Ubuntu devices, or `sudo service docker start` on Amazon Linux. Other platforms may have other commands to start the daemon. This code was tested on Ubuntu.

3. Clone this repository and cd to the parent directory.

4. Run the following command
```
GGP -g groupname -a architecture -d deployments/python-docker.conf --script
```
where GGP is your provisioner method of choice (docker or jar), groupname is the name of the group you'd like to create, and architecture is the architecture of your core device, either X86_64 or ARM32 or ARM64.

5. Copy the generated script located in ./build to your core device and run it.

6. Install python3.7 on your GG Core device. This lambda runs as root without containerization, so you should install it separately. `sudo apt install python3.7` works with apt. You may have to `pip3 install greengrasssdk`, and make sure that when you run `pip3 -V` the output corresponds to python 3.7

> NOTE: This code is also compatible with python2.7. If the 2.7 runtime is preferred, change the function.conf line to `language = "PYTHON2_7"`. You'll have to install python2.7 on your GG Core device similarly to the instructions in step 6.

7. In the AWS console, subscribe to the topics `NAME_OF_CORE/docker/logs` and `NAME_OF_CORE/docker/info`. Note that the name of the core is the name of the group concatenated with `"_Core"`

8. Open the AWS IoT Core console. Hit Greengrass in the left menu. Select the submenu groups. Select your group. Go to Cores and select your core. Hit shadow, then, edit, and delete anything already there. Paste in the contents of example_container_config.json

> NOTE: You must run docker containers that are compatible with your architecture. Recommended containers are "bfirsh/reticulate-splines" for X86_64 Core devices and "cea2aj/reticulate-splines-arm" for ARM32 Core devices. These are containers that print a simple counter message every second. Replace the "image_name" field in the example_container_config.json file to switch containers.

9. If you already have these docker images pulled to your Core device, awesome! If not, change the "use_local" field to false in the configuration.

10. In the `NAME_OF_CORE/docker/info` topic you'll see diagnostic messages from the lambda, in `NAME_OF_CORE/docker/logs` you'll see logs read directly from the standard output of the containers. These logs have been forwarded over MQTT by the lambda.

11. Repeat step 8, but try changing a configuration option, like timeout. You should see new containers start with the new configuration.

12. If you instead paste in an identical configuration and save the shadow, nothing happens. There is no difference between desired and reported states, so no work is done.
