# LatencyTesterPython3.py

import json
import logging
import os
import time
import uuid
from builtins import int, round, range, len, str

import greengrasssdk
import ping3

current_milli_time = lambda: int(round(time.time() * 1000))

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

THING_NAME = os.environ['AWS_IOT_THING_NAME']

first_underscore = 5

DEBUG_TOPIC = THING_NAME + '/python3/latency_tester/debug'

hosts = {}

for host in os.environ:
    if not host.startswith("HOST_"):
        continue
    host_value = os.getenv(host)
    second_underscore = host.index("_", first_underscore)
    host_number = host[first_underscore:second_underscore]
    host_number = int(host_number)
    host_name = host[second_underscore + 1:]
    host_name = host_name.replace("_", " ")
    hosts[host_number] = {}
    hosts[host_number]['name'] = host_name
    hosts[host_number]['address'] = host_value

number_of_hosts = len(hosts)
hosts_array = []

for index in range(1, number_of_hosts + 1):
    hosts_array.append(hosts[index])

GPIO_TOPIC = THING_NAME + '/python3/raspberrypi/gpio'
OUTPUT_TOPIC = THING_NAME + '/python3/latency_tester/output'


def check_host_with_ping(address):
    seconds = ping3.ping(address)
    return seconds * 1000


def function_handler(event, context):
    latencies = []

    for host in hosts_array:
        milliseconds = check_host_with_ping(host['address'])
        host_latency = {}
        host_latency['name'] = host['name']
        host_latency['unit'] = 'Milliseconds'
        host_latency['value'] = milliseconds
        latencies.append(host_latency)

    # Downstream consumers require a map
    latency = {}
    latency['uuid'] = str(uuid.uuid4())
    latency['latencies'] = latencies

    client.publish(topic=OUTPUT_TOPIC, payload=json.dumps(latency))

    return
