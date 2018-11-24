# Socket Server Python

## What is this function?

This function creates a TCP socket server on port 8001 and relays the messages it receives on that socket to AWS IoT

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

When the function receives a message on socket it will publish the data it receives on the `${AWS_IOT_THING_NAME}/socketserver/output` topic that looks like this:

```json
{
  "message": "message from client"
}
```
