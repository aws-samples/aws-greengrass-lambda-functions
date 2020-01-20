# Shared File 1 Python 3

## What is this function?

This function, along with Shared File 2 Python 3, demonstrates how functions can share paths on the host
by using the same local volume resources. This function will randomly write data to a shared file that
the other function looks for. It will also read data from a shared file that the other function writes to.

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

When the other function writes to the shared output file a message will be sent on the `${AWS_IOT_THING_NAME}/python3/shared/1` topic that looks like this:

```
{
  "group_id": "d27247c1-c714-4f9d-81bc-70dbf8b7a5c2",
  "thing_name": "xxxxxxxxxxxxxxx_Core",
  "thing_arn": "arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/xxxxxxxxxxxxxxx_Core",
  "message": "Data found in shared input file [/tmp/shared2.txt], it was [5.23]"
}
```
