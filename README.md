## AWS Greengrass Lambda Functions

Example local Lambda functions that can be used with AWS Greengrass and the AWS Greengrass Provisioner.  This repo contains
the functions and the deployment configurations to launch those functions in different configurations.

## How do I launch these functions with the provisioner?

Step 1: Clone this repo

Step 2: [Read the provisioner command-line examples](https://github.com/awslabs/aws-greengrass-provisioner/blob/master/docs/CommandLine.md)
## Current function list

- Python
  - [BenchmarkPython](functions/BenchmarkPython) - a naive benchmark that creates a pinned function that sends messages to itself
  - [HTTPPython](functions/HTTPPython) - sends HTTP requests from the core to any address (local network or otherwise), triggered by MQTT messages from the cloud
  - [HelloWorldPython2](functions/HelloWorldPython2) - Hello, World in Python 2
  - [HelloWorldPython3](functions/HelloWorldPython3) - Hello, World in Python 3
  - [HelloWorldPythonWithCloudFormation](functions/HelloWorldPythonWithCloudFormation) - Hello, World in Python with a CloudFormation template that demonstrates how to build republish rules that the provisioner can launch automatically
  - [LiFXPython](functions/LiFXPython) - control LiFX bulbs
  - [SocketServerPython](functions/SocketServerPython) - an example of how to listen on a socket in Python and relay the inbound TCP messages to the cloud via MQTT
  - [DockerPython](functions/DockerPython) - shows how to control Docker with Greengrass in Python using device shadows

- NodeJS
  - [HelloWorldNode](functions/HelloWorldNode) - Hello, World in Node
  - [HTTPNode](functions/HTTPNode) - sends HTTP requests from the core to any address (local network or otherwise), triggered by MQTT messages from the cloud
  - [WebServerNode](functions/WebServerNode) - an example of how to create an Express web server in a pinned Lambda function

- Java with Cloud Device Driver framework
  - [CDDSkeletonJava](functions/CDDSkeletonJava) - shows how the Java Cloud Device Driver framework can be used
  - [CDDDMIJava](functions/CDDDMIJava) - relays Desktop Management Interface (DMI) information to the cloud when requested via MQTT
  - [CDDBenchmarkJava](functions/CDDBenchmarkJava) - a naive Java benchmark that creates a pinned function that sends messages to itself
  - [CDDSenseHatJava](functions/CDDSenseHatJava) - shows how to control a SenseHat display on a Raspberry Pi
  - [CDDDockerJava](functions/CDDDockerJava) - shows how to control Docker with Greengrass

- C
  - [ARM32SampleC](functions/ARM32SampleC) - Hello World in C for ARM32 architectures
  - [X86_64SampleC](functions/X86_64SampleC) - Hello World in C for X86_64 architectures

## License Summary

This sample code is made available under a modified MIT license. See the LICENSE file.
