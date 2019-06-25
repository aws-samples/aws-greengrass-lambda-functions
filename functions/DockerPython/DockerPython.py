# readcontainer.py

import json
import logging
import os
import requests

import greengrasssdk
import docker

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')
docker_client = docker.from_env()
logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

THING_NAME = os.environ['AWS_IOT_THING_NAME']

base_docker_topic = THING_NAME +"/docker"
invocation_topic = base_docker_topic + '/invoke/list'
info_topic = base_docker_topic + '/info/list'

def function_handler(event, context):
    inbound_topic = context.client_context.custom['subject']

    # if not inbound_topic.startswith(request_topic):
    #     logger.info('Inbound topic is not the request topic hierarchy %s %s', inbound_topic, request_topic)
    #     return
    reply = {}
    reply['message'] = "greetings from dorcker"
    reply['containerlist'] = docker_client.containers.list()
    json_reply = json.dumps(reply)
    logger.info("hfqdsalkjhdflkjadshflkhdsaflkjshdaflkjds")
    logger.info('Publishing reply %s', json_reply)
    client.publish(topic=info_topic, payload=json_reply)
    return
