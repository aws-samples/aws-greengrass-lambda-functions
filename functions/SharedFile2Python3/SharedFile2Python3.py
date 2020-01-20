#!/usr/bin/env python3

import json
import logging
import os
import random
import sys
from threading import Timer

import greengrasssdk

# Create an IoT data client with the Greengrass SDK
client = greengrasssdk.client('iot-data')

# Set up the logger so it looks nice
logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

SHARED_INPUT_FILE = os.environ['SHARED_INPUT_FILE']
SHARED_OUTPUT_FILE = os.environ['SHARED_OUTPUT_FILE']
OUTPUT_TOPIC = os.environ['OUTPUT_TOPIC']

GROUP_ID = os.environ['GROUP_ID']
THING_NAME = os.environ['AWS_IOT_THING_NAME']
THING_ARN = os.environ['AWS_IOT_THING_ARN']

payload = {}
payload['group_id'] = GROUP_ID
payload['thing_name'] = THING_NAME
payload['thing_arn'] = THING_ARN

RAND_MIN = 1
RAND_MAX = 10
RAND_TARGET = RAND_MIN


def greengrass_internal_handler():
    try:
        # Generate a random integer, endpoints included
        if random.randint(RAND_MIN, RAND_MAX) == RAND_TARGET:
            # 10% of the time we're going to write some random data in the shared output file
            shared_output_file = open(SHARED_OUTPUT_FILE, 'w')
            shared_output = f"Random output from other function [{random.uniform(RAND_MIN, RAND_MAX):.2f}]"
            logger.info(f"Writing random output string [{shared_output}] to shared output file [{SHARED_OUTPUT_FILE}]")
            shared_output_file.write(shared_output)
            shared_output_file.close()

        if not os.path.exists(SHARED_INPUT_FILE):
            # Try again in 1 second
            logger.info('No data waiting in shared file, sleeping...')
            Timer(5, greengrass_internal_handler).start()
            return

        with open(SHARED_INPUT_FILE) as input:
            shared_input = input.read()

        # Delete the file so we don't pick it up again
        os.remove(SHARED_INPUT_FILE)

        # Send the payload to the expected topic
        payload['message'] = f"Data found in shared input file [{SHARED_INPUT_FILE}], it was [{shared_input}]"
        client.publish(topic=OUTPUT_TOPIC, payload=json.dumps(payload))
    except:
        # An exception occurred, log it and then retry the function
        e = sys.exc_info()[0]
        logger.error(f"Exception: {e}")

    # Asynchronously schedule this function to be run again in 5 seconds
    Timer(5, greengrass_internal_handler).start()


# Start executing the function above
greengrass_internal_handler()


# This is a dummy handler and will not be invoked
# Instead the code above will be executed in an infinite loop for our example
def function_handler(event, context):
    return
