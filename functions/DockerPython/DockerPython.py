# DockerPython.py
# Demonstrates an alternative to CDDDockerJava for managing docker containers

import json
import logging
import os
import platform
import docker
import threading
# from threading import Timer
import greengrasssdk

# Name of containers to pull, read from, and manage
MY_IMAGE_NAMES = ["armhf/hello-world"]

# Create a greengrass core sdk client
client = greengrasssdk.client('iot-data')

# create client for interacting with docker
# additional options may be needed if networking containers
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
info_topic = base_docker_topic + '/info'
log_topic = base_docker_topic + '/logs'



# publishes info json to the THING_NAME/docker/info topic
# for general information from the lambda
def send_info(payload_json):
    client.publish(topic=info_topic, payload=json.dumps(payload_json))

# publishes log json to the THING_NAME/docker/log topic
# ONLY for logs from docker containers
def send_log(payload_json):
    client.publish(topic=log_topic, payload=json.dumps(payload_json))

# Kill and remove all running containers upon lambda startup
def kill_all_containers():
    all_containers = docker_client.containers.list()
    for container in all_containers:
        kill_msg = {"message":"Killing and removing container: " + container.name}
        send_info(kill_msg)
        container.stop()
        container.remove()
    survival_msg = {"message":"Containers surviving: " + str(docker_client.containers.list())}
    send_info(survival_msg)

# pull a single image from dockerhub using it's string name
# TODO: ECR integration
def pull_image(image_name):
    pull_msg = {"message":"Pulling container: " + image_name}
    send_info(pull_msg)

    docker_client.images.pull(image_name)

    pull_msg = {"message":"Pulled container: " + image_name}
    send_info(pull_msg)

# Run an arbitrary number of containers from the same image
def run_containers(image_name, number_to_run):
    for i in range(number_to_run):
        container = docker_client.containers.run(image_name, detach=True)

# Run exactly one container of a given image
def run_single_container(image_name):
    run_containers(image_name, 1)

# Continually read and publish the logs of a container
def log_stream_worker(container):
    for line in container.logs(stream=True):
        payload['message'] = 'Docker running with output {}.'.format(line.strip())
        payload['container_name'] = container.name
        send_log(payload)
        
# creates log threads
# TODO: kill threads gracefully
def spawn_all_logs():
    all_containers = docker_client.containers.list()
    for container in all_containers:
        t = threading.Thread(target=log_stream_worker, args=(container,))
        t.start()

# ALL execution begins here, excepting the dummy function_handler below
def main():
    send_info({"message":"Lambda starting. Executing main..."})
    kill_all_containers()
    for image_name in MY_IMAGE_NAMES:
        send_info({"message":"Working on image " + image_name + "."})
        pull_image(image_name)
        run_single_container(image_name)
    spawn_all_logs()

main()

# This is a dummy handler and will not be invoked
# Instead the main function below will be executed
def function_handler(event, context):
    return