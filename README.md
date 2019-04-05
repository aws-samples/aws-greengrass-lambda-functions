## AWS Greengrass Lambda Functions

Example local Lambda functions that can be used with AWS Greengrass and the AWS Greengrass Provisioner.  This repo contains
the functions and the deployment configurations to launch those functions in different configurations.

## How do I launch these functions with the provisioner?

Step 1: Clone this repo

Step 2: [Read the provisioner command-line examples](https://github.com/awslabs/aws-greengrass-provisioner/blob/master/docs/CommandLine.md)
## Current function list

- Python
  - BenchmarkPython - a naive benchmark that creates a pinned function that sends messages to itself
  - HTTPPython - sends HTTP requests from the core to any address (local network or otherwise), triggered by MQTT messages from the cloud
  - HelloWorldPython - Hello, World in Python
  - HelloWorldPythonWithCloudFormation - Hello, World in Python with a CloudFormation template that demonstrates how to build republish rules that the provisioner can launch automatically
  - LiFXPython - control LiFX bulbs
  - SocketServerPython - an example of how to listen on a socket in Python and relay the inbound TCP messages to the cloud via MQTT

- NodeJS
  - HelloWorldNode - Hello, World in Node

- Java with Cloud Device Driver framework
  - CDDSkeletonJava - shows how the Java Cloud Device Driver framework can be used
  - CDDDMIJava - relays Desktop Management Interface (DMI) information to the cloud when requested via MQTT
  - CDDBenchmarkJava - a naive Java benchmark that creates a pinned function that sends messages to itself

## License Summary

This sample code is made available under a modified MIT license. See the LICENSE file.
