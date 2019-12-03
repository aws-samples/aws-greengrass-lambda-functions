# Hello World Python 3

## What is this function?

This function is the standard Python 3 Hello World function from the Greengrass documentation.  The
output has been modified slightly so that it adds some environment variables to its output.  These
environment variables are added to the environment of all Lambda functions created with
GG-provisioner.  They are:

* `GROUP_ID` - The group ID of the current Greengrass group (e.g. `fe8a6893-c88a-4ba4-9016-8a1d61a7b2a7`)
* `AWS_IOT_THING_NAME` - The thing name of the current Greengrass Core
* `AWS_IOT_THING_ARN` - The thing ARN of the current Greengrass Core

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

Every 5 seconds a message is sent on `${AWS_IOT_THING_NAME}/python3/hello/world` topic that looks like this:

```
{
  "group_id": "d27247c1-c714-4f9d-81bc-70dbf8b7a5c2",
  "thing_name": "xxxxxxxxxxxxxxx_Core",
  "thing_arn": "arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/xxxxxxxxxxxxxxx_Core", "message": "Hello world! Sent from Greengrass Core from Python 3.7.3 running on platform Linux-4.15.0-1031-aws-x86_64-with-Ubuntu-18.04-bionic d27247c1-c714-4f9d-81bc-70dbf8b7a5c2 xxxxxxxxxxxxxxx_Core arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/xxxxxxxxxxxxxxx_Core",
  "message": "Hello world! Sent from Greengrass Core from Python ..."
}
```
