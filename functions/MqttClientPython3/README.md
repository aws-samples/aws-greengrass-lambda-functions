# MQTT Client Python 3

## What is this function?

This function is a Python 3 function that will connect to an MQTT server, subscribe to a topic, and
republish the messages it receives to Greengrass.

The following environment variables are used to configure the client:

* `HOST` - Specifies the hostname or IP address of the MQTT server
* `PORT` - Specifies the port that the MQTT server is expected to be listening on
* `USERNAME` - (Optional) The username to use to connect to the MQTT server. If this is blank no authentication will be used.
* `PASSWORD` - (Optional) The password to use to connect to the MQTT server. If this is blank and the username is set an empty password will be sent.

## What does the output look like?

Any time a message is sent on any topic it will be republished on the same topic on Greengrass. This assumes that the connection to the specified MQTT server succeeds, optional authentication is accepted, and the subscription is allowed,

## To quickly test with default settings...

If you have Docker installed you can start a Mosquitto server with no authentication like this:

```bash
docker run -it -p 1883:1883 -p 9001:9001 eclipse-mosquitto
```

After it starts, deploy this function to your core and let it connect on localhost to Mosquitto in the Docker container.

Finally, publish a test message to Mosquitto by running this command from the host running the Docker container:

```bash
mosquitto_pub -h localhost -t test -m test
```

If `mosquitto_pub` is not installed and the system is running Ubuntu you can install it with this command:

```bash
sudo apt install -y mosquitto-clients
```

Once the `mosquitto-clients` package is installed try running the `mosquitto_pub` command again.

If the message is published to the Mosquitto server in the Docker container successfully it will be republished to the AWS IoT Core
message broker in the account that Greengrass is running in. The message will be on the same topic, with the same content.

If the message does not arrive at the AWS IoT Core message broker it may have been sent on a topic that the Greengrass Core can not
access due to it's policy. Check the policy for the Greengrass Core, adjust as necessary and try again. Otherwise, try publishing
on a topic that the Greengrass Core is known to have access to.
