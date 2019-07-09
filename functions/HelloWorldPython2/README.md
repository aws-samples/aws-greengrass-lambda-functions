# Hello World Python

## What is this function?

This function is the standard Python 2 Hello World function from the Greengrass documentation.  The
output has been modified slightly so that it adds some environment variables to its output.  These
environment variables are added to the environment of all Lambda functions created with
GG-provisioner.  They are:

* `GROUP_ID` - The group ID of the current Greengrass group (e.g. `fe8a6893-c88a-4ba4-9016-8a1d61a7b2a7`)
* `AWS_IOT_THING_NAME` - The thing name of the current Greengrass Core
* `AWS_IOT_THING_ARN` - The thing ARN of the current Greengrass Core

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

Every 5 seconds a message is sent on `${AWS_IOT_THING_NAME}/python2/hello/world` topic that looks like this:

```
{"thing_arn": "arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/xxxxxxxxxxxx_Core", "message": "Hello world! Sent from Greengrass Core from Python 2.7.15+ running on platform Linux-4.15.0-1031-aws-x86_64-with-Ubuntu-18.04-bionic facade34-9dbc-4eff-8f9a-ee1fda7a5017 xxxxxxxxxxxx_Core arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/xxxxxxxxxxxx_Core", "group_id": "facade34-9dbc-4eff-8f9a-ee1fda7a5017", "thing_name": "xxxxxxxxxxxx_Core"}
```
