# Benchmark Python

## What is this function?

This function is used to benchmark Python performance on a Greengrass Core.  It publishes messages
as fast as it can on the `${AWS_IOT_THING_NAME}/cdd/benchmark/output/python` topic and publishes the summary data about
the number of messages it can publish per second on the `${AWS_IOT_THING_NAME}/cdd/benchmark/results/python` topic.

This is a long-lived Lambda function that does not require any interaction.  It will start
immediately when your core starts.

The `${AWS_IOT_THING_NAME}/cdd/benchmark/results/python` topic is routed to the cloud so you can view it in the AWS IoT
console.

## What does the output look like?

Here is a sample record from a Raspberry Pi 3:

```json
{
  "seconds": 30.022,
  "messagesPerSecond": 31.310372393578046,
  "messagesPublished": 940,
  "errors": 0
}
```