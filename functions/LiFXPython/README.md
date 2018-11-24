# LiFX Python

## What is this function?

This function discovers and controls LiFX WiFi bulbs.

## What does the output look like?

### Scanning for bulbs

When the function receives a message on the `cdd/lifx/command` topic that looks like this:

```json
{
  "action": "scan"
}
```

It will respond with a list of the LiFX WiFi bulbs it has discovered on the `cdd/lifx/status` topic like this:

```json
[
  "Office light",
  "Two bulb light #1",
  "Two bulb light #2",
  "Tall light",
  "Light on table"
]
```

### Turning off bulbs

When the function receives a message on the `cdd/lifx/command` topic that looks like this:

```json
{
  "action": "off",
  "bulb": "Tall light"
}
```

Only one bulb can be specified per message.

It will attempt to turn off the LiFX WiFi bulb named "Tall light".  No status is reported back from this command.

### Turning on bulbs

When the function receives a message on the `cdd/lifx/command` topic that looks like this:

```json
{
  "action": "on",
  "bulb": "Tall light",
  "color": "0, 0, 100, 9000"
}
```

Only one bulb can be specified per message.

It will attempt to turn on the LiFX WiFi bulb named "Tall light".  It will
set the hue, saturation, brightness, and kelvin to 0, 0, 100, and 9000, respectively.

If you leave out the color value it will default to 0, 0, 100, 9000.
