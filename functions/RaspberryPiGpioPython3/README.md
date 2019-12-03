# Raspberry Pi GPIO (Python 3)

## What is this function?

This function shows how to trigger actions based on GPIO events. This function is different from the Greengrass GPIO
connector in that it does not poll for state changes. It uses RPi.GPIO's event support with callbacks.

NOTE: RPi.GPIO is not included in requirements.txt as it is expected to already be on the host. If it is included there
will be errors showing that "_GPIO" cannot be found. Also, non-Raspberry Pi platforms often cannot install this library.

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

Every time a button is pressed a message is sent on `${AWS_IOT_THING_NAME}/python3/raspberry/gpio` topic that looks like this:

```
{
    "gpio": 3,
    "group_id": "d23b6fb6-5bc7-40ff-9c3b-6eb3a2088a61",
    "message": "Pressed or released",
    "thing_arn": "arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/xxxxxxxxxx_Core",
    "thing_name": "xxxxxxxxxx_Core"
}
```
