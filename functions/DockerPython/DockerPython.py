# DockerPython.py
# Demonstrates an alternative to CDDDockerJava for managing docker containers

import json
import logging
import os
import platform
import docker
from threading import Timer
import greengrasssdk

# Name of container to pull, read from, and manage
MY_IMAGE_NAMES = ["armhf/hello-world"]

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

# client for interacting with docker
docker_client = docker.from_env()

# Retrieving platform information to send from Greengrass Core
my_platform = platform.platform()

# Logging setup
logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

# get environmental variables
GROUP_ID = os.environ['GROUP_ID']
THING_NAME = os.environ['AWS_IOT_THING_NAME']
THING_ARN = os.environ['AWS_IOT_THING_ARN']

# initialize response
payload = {}
payload['group_id'] = GROUP_ID
payload['thing_name'] = THING_NAME
payload['thing_arn'] = THING_ARN


# setup OUTBOUND topics
base_docker_topic = THING_NAME +'/docker'
logging_topic = base_docker_topic + '/logs'
info_topic = base_docker_topic + '/info'

# Kill All running containers upon lambda startup
def kill_all_containers():
    all_containers = docker_client.containers.list()
    for container in all_containers:
        kill_msg = {"message":"Killing and removing container: " + container.name}
        client.publish(topic=info_topic, payload=json.dumps(kill_msg))
        container.stop()
        container.remove()
    survival_msg = {"message":"Containers surviving: " + str(docker_client.containers.list())}
    client.publish(topic=info_topic, payload=json.dumps(survival_msg))

def pull_image(image_name):
    pull_msg = {"message":"Pulling container: " + MY_CONTAINER}
    client.publish(topic=info_topic, payload=json.dumps(pull_msg))
    docker_client.images.pull(MY_CONTAINER)
    pull_msg = {"message":"Pulled container: " + MY_CONTAINER}
    client.publish(topic=info_topic, payload=json.dumps(pull_msg))
# Run an arbitraruy number of containers from the same image
def run_containers(image_name, number_to_run):
    for i in range(number_to_run):
        container = docker_client.containers.run(MY_CONTAINER,devices=["/dev/vchiq:/dev/vchiq:rwm","/dev/vcsm:/dev/vcsm:rwm"],detach=True)
# Run exactly one container of a given image
def run_single_container(image_name):
    run_containers(image_name, 1)
# Continually read and publish the logs of a container
def stream_logs(container)
    for line in container.logs(stream=True):
        payload['message'] = 'Docker running with output {}.'.format(line.strip())
        client.publish(topic=logging_topic, payload=json.dumps(payload))


# This is a dummy handler and will not be invoked
# Instead the main function below will be executed
def function_handler(event, context):
    return

def main():
    kill_all_containers()
    for image_name in MY_IMAGE_NAMES:
        pull_container(image_name)
        run_single_container(image_name)

main()