## AWS Greengrass Lambda Functions

Example local Lambda functions that can be used with AWS Greengrass and the AWS Greengrass Provisioner.  This repo contains
the functions and the deployment configurations to launch those functions in different configurations.

## Current function list

- BenchmarkPython - a naive benchmark that creates a pinned function that sends messages to itself
- CDDSkeletonJava - shows how the Java Cloud Device Driver framework can be used
- HTTPPython - sends HTTP requests from the core to any address (local network or otherwise), triggered by MQTT messages from the cloud
- HelloWorldNode - Hello, World in Node
- HelloWorldPython - Hello, World in Python
- HelloWorldPythonWithCloudFormation - Hello, World in Python with a CloudFormation template that demonstrates how to build republish rules that the provisioner can launch automatically
- LiFXPython - control LiFX bulbs
- SocketServerPython - an example of how to listen on a socket in Python and relay the inbound TCP messages to the cloud via MQTT
- CDDDMIJava - relays Desktop Management Interface (DMI) information to the cloud when requested via MQTT

## License Summary

This sample code is made available under a modified MIT license. See the LICENSE file.
