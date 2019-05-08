# CDD SenseHat Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This function allows users to utilize the SenseHat hardware on the Raspberry Pi from Greengrass
and AWS IoT.  In `function.conf` a local device resource for the `/dev/fb1` device is included.
This gives the Lambda function direct access to `/dev/fb1` to write framebuffer data.

## What is supported?

Currently only a few canned animations can be started on the SenseHat LED array.

You can obtain a list of the available animations by sending any JSON message to the
`${AWS_IOT_THING_NAME}/cdd/sensehat/animation/list` topic.  You will receive a response on the
`${AWS_IOT_THING_NAME}/cdd/sensehat/animation/list/output` topic that looks like this:

```json
{
  "animations": [
    "Blank",
    "Counter",
    "DisplayNumber",
    "Fire",
    "GlitchNumber",
    "RandomStatic",
    "SpinNumber"
  ]
}
```

To start any of these animations send any JSON message to the topic
`${AWS_IOT_THING_NAME}/cdd/sensehat/animation/start/ANIMATION_NAME` where `ANIMATION_NAME` is one of the animations in
the list.  Names are case sensitive so `Fire` will work but `fire` will not.

To stop the animation send any JSON message to the topic `${AWS_IOT_THING_NAME}/cdd/sensehat/animation/stop`.

## Is there a quick way to see if it is working?

Yes. There are several convenience scripts you can use to see if the function is working for you.

- `blank.sh` - Clears the LED array
- `send-list-animations-message.sh` - Sends a message to the list topic and the device should respond with an MQTT message. You'll need to be subscribed to the topic with an MQTT client to see the response. This script only sends the message.
- `start-counter.sh` - Starts the counter animation
- `start-display-number.sh` - Starts the display number animation
- `start-fire-animation.sh` - Starts the fire animation
- `start-glitch-number.sh` - Starts the glitch number animation
- `start-random-static.sh` - Starts the random static animation
- `start-spin-number.sh` - Starts the spin number animation

## What do you plan on supporting?

* Reading input events from the joystick
* Sending parameters to the animations
* Any other sensors we can find documentation for
