# DockerPython

## What is this function?

This function presents an alternate approach to the CDDDockerJava function for managing containers with lambdas. It's goal is to reduce 
reliance on MQTT messaging by making every lambda deployment correspond directly to a container pull.

## Setup

1. Clone this repository.
2. Run the following command
```
GGP -g test-group -a X86_64 -d deployments/python-docker.conf --script
```
where GGP is your provisioner method of choice (docker or jar)

3. Copy the generated script in ./build