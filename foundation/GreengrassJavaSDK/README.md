# CDD Baseline Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.

## What does it provide?

CDD provides an event bus driven programming model for developing Greengrass Lambda functions in Java.  The event types
it provides are:

- GreengrassLambdaEvent - published to the event bus when an MQTT message is received on a topic
- GreengrassStartEvent - published to the event bus when Greengrass starts

It also provides standardized ways to publish JSON and binary messages, launch external processes, create MQTT topic
hierarchies that incorporate the Greengrass group's core name, and can provide a partially simulated environment to make
local debugging easier.

## What does it simplify?

The features listed above simplify getting started with Greengrass Lambda development.  The baseline example function
called CDDSkeleton provides a template that can be used to create more complex functions quickly.

# Want to know more?

Fill out a Github issue and let us know what additional documentation you'd like to see.
