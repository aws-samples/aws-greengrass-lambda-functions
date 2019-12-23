import logging
import os
from threading import Timer

import greengrasssdk
import paho.mqtt.client as mqtt

# Creating a greengrass core sdk client
iot_data = greengrasssdk.client('iot-data')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

GROUP_ID = None
THING_NAME = None
THING_ARN = None
HOST = 'localhost'
PORT = 1883
USERNAME = None
PASSWORD = None

if 'USERNAME' in os.environ:
    USERNAME = os.environ('USERNAME')

if 'PASSWORD' in os.environ:
    PASSWORD = os.environ('PASSWORD')


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("#")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    # Publish back to Greengrass
    topic = msg.topic
    logger.info('Publishing message to Greengrass from broker on topic [' + topic + ']')
    iot_data.publish(topic=topic, payload=msg.payload)


mqtt_client = mqtt.Client()
mqtt_client.on_connect = on_connect
mqtt_client.on_message = on_message

mqtt_client.username_pw_set(USERNAME, PASSWORD)


# This is invoked when a message is received from Greengrass
def function_handler(event, context):
    inbound_topic = context.client_context.custom['subject']

    # Publish the message to the MQTT server
    logger.info('Publishing message to broker from Greengrass on topic [' + inbound_topic + ']')
    mqtt_client.publish(inbound_topic, event)

    return


def greengrass_mqtt_client_loop():
    error = None

    try:
        mqtt_client.connect(HOST, PORT, 60)

        # Blocking call that processes network traffic, dispatches callbacks and handles reconnecting.
        mqtt_client.loop_forever()
    except ConnectionRefusedError as e:
        # Connection refused, try connecting again in 5 seconds
        error = 'Connection to the MQTT server failed [' + str(e) + '], make sure it is running and the settings are correct'
    except Exception as e:
        # Something else went wrong
        error = 'Something went wrong [' + str(e) + ']'

    # If we get here we'll start log the error, if any, and loop again
    if error is not None: logger.error(error)
    logger.error('Reconnecting')
    Timer(5, greengrass_mqtt_client_loop).start()


# Asynchronously schedule this function to run in 5 seconds. We don't call the function immediately in case the MQTT
#   configuration is incorrect.
Timer(5, greengrass_mqtt_client_loop).start()
