# Latency tester (Python 3)

## What is this function?

This function makes ping requests from the Greengrass Core and reports the round trip time of those requests.

Any time this function receives a message on any topic it will ping each host in the environment variables that start
with `HOST_`.

NOTE: This function must run outside of the Greengrass container and must run as root so that it can create ICMP packets.

## What should the environment variables look like?

The environment variables block in the function.conf should look like this:

```
  environmentVariables = {
    HOST_1_Default_gateway = "192.168.1.1"
    HOST_2_AWS_Virginia_console = "console.aws.amazon.com"
    HOST_3_AWS_Oregon_console = "us-west-2.console.aws.amazon.com"
    HOST_4_AWS_Singapore_console = "ap-southeast-1.console.aws.amazon.com"
  }
```

The format of the variable name is: `HOST_X_NAME` where `X` is a number and `NAME` is the name that should be included
with the round trip time reports. `NAME` will have all underscores replaced with spaces.

The numbers for `X` must start at 1, must be unique, and the numbers must be sequential with no gaps (e.g. 1, 2, 4 is invalid).

The value for each variable is the host name or IP address that should be pinged. If DNS resolution may be an issue these
values should only be IP addresses.

## What will the output look like?

```json
{
    "latencies": [
        {
            "name": "Default gateway",
            "unit": "Milliseconds",
            "value": 1.1777877807617188
        },
        {
            "name": "AWS Virginia console",
            "unit": "Milliseconds",
            "value": 9.070634841918945
        },
        {
            "name": "AWS Oregon console",
            "unit": "Milliseconds",
            "value": 67.3367977142334
        },
        {
            "name": "AWS Singapore console",
            "unit": "Milliseconds",
            "value": 236.07969284057617
        }
    ],
    "uuid": "d0773bbf-351e-4cf9-b198-057bdf10e53d"
}
```

`latencies` is an array of objects. Each object contains a `name`, `unit`, and `value`. These values were chosen because
they are values that can be used easily for CloudWatch metrics and dashboards.

`uuid` is a unique UUID value for each message in case a downstream system needs to be able to de-duplicate the messages.
