# CDD Benchmark Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This function is used to benchmark Java performance on a Greengrass Core.  It publishes messages
as fast as it can on the `${AWS_IOT_THING_NAME}/cdd/benchmark/output/java` topic and publishes the summary data about
the number of messages it can publish per second on the `${AWS_IOT_THING_NAME}/cdd/benchmark/results/java` topic.

This is a long-lived Lambda function that does not require any interaction.  It will start
immediately when your core starts.

The `${AWS_IOT_THING_NAME}/cdd/benchmark/results/java` topic is routed to the cloud so you can view it in the AWS IoT
console.

## What does the output look like?

Here is a sample record from a Raspberry Pi 3:

```json
{
  "seconds": 95.848,
  "messagesPublished": 727,
  "messagesPerSecond": 7.584926133043987,
  "errors": 0
}
```