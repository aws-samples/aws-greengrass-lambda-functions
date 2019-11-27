import json
import logging
import os
import platform
from threading import Timer

import RPi.GPIO as GPIO

SDA_PIN = 3

import greengrasssdk

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

# Retrieving platform information to send from Greengrass Core
my_platform = platform.platform()
python_version = platform.python_version()

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

# When deployed to a Greengrass core, this code will be executed immediately
# as a long-lived lambda function.  The code will enter the infinite while loop
# below.
# If you execute a 'test' on the Lambda Console, this test will fail by hitting the
# execution timeout of three seconds.  This is expected as this function never returns
# a result.

GROUP_ID = os.environ['GROUP_ID']
THING_NAME = os.environ['AWS_IOT_THING_NAME']
THING_ARN = os.environ['AWS_IOT_THING_ARN']

BASE_TOPIC = '/python3/raspberrypi'

DEBUG_TOPIC = BASE_TOPIC + '/debug'
DEBUG_TOPIC = THING_NAME + DEBUG_TOPIC

OUTPUT_TOPIC = BASE_TOPIC + '/gpio'
OUTPUT_TOPIC = THING_NAME + OUTPUT_TOPIC


def get_payload():
    payload = {}
    payload['group_id'] = GROUP_ID
    payload['thing_name'] = THING_NAME
    payload['thing_arn'] = THING_ARN

    return payload


def my_callback(channel):
    payload = get_payload()
    payload['gpio'] = channel
    payload['message'] = 'Pressed or released'

    client.publish(topic=OUTPUT_TOPIC, payload=json.dumps(payload))


def setup():
    payload = get_payload()

    payload['message'] = 'Starting up'
    client.publish(topic=DEBUG_TOPIC, payload=json.dumps(payload))

    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(SDA_PIN, GPIO.IN)

    # Add edge detection on a channel, ignoring further edges for 200ms for switch bounce handling
    GPIO.add_event_detect(SDA_PIN, GPIO.FALLING, bouncetime=200)
    GPIO.add_event_callback(SDA_PIN, callback=my_callback)

    payload['message'] = 'Start up complete'
    client.publish(topic=DEBUG_TOPIC, payload=json.dumps(payload))

    # Asynchronously schedule the do nothing function to be run in 5 seconds
    Timer(5, do_nothing).start()


def do_nothing():
    # Do nothing

    payload = get_payload()
    payload['message'] = 'Still running'
    client.publish(topic=DEBUG_TOPIC, payload=json.dumps(payload))

    # Asynchronously schedule this function to be run again in 5 seconds
    Timer(5, do_nothing).start()


# Do the setup
setup()


# This is a dummy handler and will not be invoked
# Instead the code above will be executed in an infinite loop for our example
def function_handler(event, context):
    return
