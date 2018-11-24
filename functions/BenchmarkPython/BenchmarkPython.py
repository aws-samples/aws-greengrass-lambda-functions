# BenchmarkPython.py

import json
import logging
import time
import threading
import os

import greengrasssdk

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

base_topic = os.environ['AWS_IOT_THING_NAME'] + '/cdd/benchmark'
output_topic = base_topic + "/output/python"
results_topic = base_topic + "/results/python"


def function_handler(event, context):
    return

def safe_publish(topic, payload):
    global errors

    try:
        client.publish(topic=topic, payload=payload)
    except:
        errors += 1

def send_results():
    global messagesPublished
    global start
    global errors

    now = int(round(time.time() * 1000))
    seconds = (now - start) / 1000.0

    reply = {}
    reply['seconds'] = seconds
    reply['messagesPublished'] = messagesPublished
    reply['messagesPerSecond'] = messagesPublished / seconds
    reply['errors'] = errors

    threading.Timer(10.0, send_results).start()
    json_reply = json.dumps(reply)
    safe_publish(results_topic, json_reply)
    logger.info(json_reply)

def do_it():
    global messagesPublished

    reply = {}
    reply['message'] = 'Benchmark'
    json_reply = json.dumps(reply)

    while True:
        messagesPublished += 1

        safe_publish(output_topic, json_reply)

messagesPublished = 0
errors = 0
start = int(round(time.time() * 1000)) - 1 # To avoid initial divide by zero
send_results()
do_it()
