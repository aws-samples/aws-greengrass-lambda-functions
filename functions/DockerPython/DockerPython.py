# HTTPPython.py

import json
import logging
import os
import requests

import greengrasssdk
import docker

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

THING_NAME = os.environ['AWS_IOT_THING_NAME']

base_topic = THING_NAME + '/docker'
request_topic = base_topic + '/invoke'
response_topic = base_topic + '/info'

def function_handler(event, context):
    reply = {}
    reply['message'] = "congrats, the data flow seems to be in order"
    json_reply = json.dumps(reply)

    logger.info('Publishing reply %s', json_reply)
    client.publish(topic=response_topic + '/' + str(id), payload=json_reply)
    return
