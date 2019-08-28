# DockerPython.py
# Demonstrates an alternative to CDDDockerJava for managing docker containers
# See README.md

import json
import logging
import os
import platform
import docker
import threading
import greengrasssdk
import boto3
import base64

# main is located at the bottom of this file
# Create a greengrass core sdk client
ggc_client = greengrasssdk.client('iot-data')

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

# initialize generic response
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
    ggc_client.publish(topic=info_topic, payload=json.dumps(payload_json).encode())

# publishes log json to the THING_NAME/docker/log topic
# ONLY for logs from docker containers
def send_log(payload_json):
    ggc_client.publish(topic=log_topic, payload=json.dumps(payload_json).encode())

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

# Clears all current containers and updates them to match
# the docker_config
def update_containers(docker_config):
    send_info({"message":"updating containers..."})
    kill_all_containers()
    for image_info in docker_config['image_config_list']:
        process_image_info(image_info)

# Work on a single image definition, ie one entry in image_config_list
def process_image_info(image_info):
    send_info({"message":"Working on image " + image_info['image_name'] + "."})
    update_status_of_container(image_info['image_name'], "yellow")
    if not image_info['use_local']:
        pull_image(image_info['image_name'])
    run_containers(image_info)

# pull a single image from dockerhub using it's string name
# TODO: ECR integration
def pull_image(image_name):
    send_info({"message":"Pulling container: " + image_name})

    docker_client.images.pull(image_name)

    send_info({"message":"Pulled container: " + image_name})


# Run an arbitrary number of containers from the same image
# According to additional options supplied by the image_info
# dictionary.
def run_containers(image_info):
    # pull information from the image_info object
    num_containers = image_info['num_containers']
    image_name = image_info['image_name']
    docker_run_args = image_info['docker_run_args']
    image_time_out = image_info['timeout']

    send_info({'message':'With image '+image_name+', running '+str(num_containers)+' containers.'})

    # repeat for multiple containers according to the info above
    for i in range(num_containers):
        # use the docker_run_args specified in the image_info as docker run's kwargs
        container = docker_client.containers.run(image_name, **docker_run_args)
        send_info({"message":"Running container with name: " + container.name + " and timeout " + str(image_time_out)})
        # Spawn a logger_timer thread. note that this in turn will spawn its own thread,
        # this is the only way I could think of doing this without extending the scope
        # of the thread information
        t = threading.Thread(target=logger_timer, args=(container, image_name, image_time_out,))
        update_status_of_container(image_name, "green")
        t.start()

# Spawns a log_stream_worker thread on container
# that is terminated after timeout
def logger_timer(container, image_name, time_out):
    stopevent = threading.Event()
    testthread = threading.Thread(target=log_stream_worker, args=(container, image_name, stopevent))
    testthread.start()
    # Join the thread after the timeout
    # regardless of exit status
    testthread.join(timeout=time_out)
    # toggle the event so the thread will stop
    # otherwise the thread would continue
    stopevent.set()
    update_status_of_container(image_name, "red")
    send_info({"message":"Container "+ container.name + " has stopped (whether by timeout or error)"})
    container.stop()
    return

# Continually read and publish the logs of a container
def log_stream_worker(container, image_name, stopevent):
    # initilize an initial payload
    container_payload = {}
    container_payload['thing_name'] = THING_NAME
    container_payload['container_name'] = container.name
    container_payload['container_output'] = ""
    container_payload['container_image'] = image_name
    # stream the container logs
    # note this for loop does not terminate unless the stopevent is set
    for line in container.logs(stream=True):
        container_payload['container_output'] += line.decode()
        if "\n" in line.decode():
            container_payload['container_output'] = container_payload['container_output'].strip()
            send_log(container_payload)
            container_payload['container_output'] = ""
        if stopevent.isSet():
            return

def convert_keys_to_string(dictionary):
    """Recursively converts dictionary keys to strings."""
    if not isinstance(dictionary, dict):
        return dictionary
    return dict((str(k), convert_keys_to_string(v))
        for k, v in dictionary.items())

# Set the status of the container in the shadow to "green" (currently running), "yellow" (starting up), or red (not running due to shut-down, pre-initialization or error)
def update_status_of_container(image_name, status):
    my_shadow = json.loads(ggc_client.get_thing_shadow(thingName=THING_NAME)['payload'])
    my_reported_shadow = my_shadow["state"]['reported']
    for container in my_reported_shadow['docker_config']['image_config_list']:
        if image_name == container["image_name"]:
            container['status'] = status
            reported_state = {
                "state": {
                    "desired": json.loads(convert_keys_to_string(json.dumps(my_reported_shadow))),
                    "reported": json.loads(convert_keys_to_string(json.dumps(my_reported_shadow)))
                }
            }
            print(reported_state)
            update_my_shadow(reported_state)
            send_info({"my_shadow":reported_state})
            return
    return "ERROR - Docker image with that name was not deployed"

# update the shadow of this AWS Thing
def update_my_shadow(json_payload):
    ggc_client.update_thing_shadow(thingName=THING_NAME, payload=json.dumps(json_payload).encode())

# Takes a desired state, updates containers, and reports new state
def update_to_desired_state(desired_state):
    # if no config present, no updates needed, at least not on our end
    if 'docker_config' not in desired_state:
        return

    desired_config = desired_state['docker_config']
    # update containers. if this fails the runtime will crash
    # so updating the reported state below will never execute

    # if update_containers succeeds, report the new state
    reported_state =  {
        "state": {
            "reported": desired_state
        }
    }
    update_my_shadow(reported_state)

    update_containers(desired_config)

# Executed upon startup of GG daemon or upon deployment of this lambda
# note: this is not the only entry point, the function_handler below
# is invoked upon shadow delta update
def main():
    send_info({"message":"Lambda starting. Executing main..."})
    ecr_cli = boto3.client('ecr', region_name='us-east-1')
    token = ecr_cli.get_authorization_token()
    username, password = base64.b64decode(token['authorizationData'][0]['authorizationToken']).decode().split(':')
    registry = token['authorizationData'][0]['proxyEndpoint']
    docker_client.login(username, password, registry=registry)
    try:
        my_shadow = json.loads(ggc_client.get_thing_shadow(thingName=THING_NAME)['payload'].decode())
    except:
        send_info({"message": "No shadow was created! Automatically generating empty shadow"})
        update_my_shadow({
            "state":{
                "desired":{"welcome": "AWS-Test"},
                "reported":{"welcome": "AWS-Test"},
            }
        })
    send_info({"my_shadow":my_shadow})
    if 'desired' in my_shadow['state']:
        desired_state = my_shadow['state']['desired']
        update_to_desired_state(desired_state)

# invoke main
main()

# handler for updates on the topic
# $aws/things/${AWS_IOT_THING_NAME}/shadow/update/delta
# which means it will be invoked whenever the shadow is changed
# "event" parameter is a description of the delta
def function_handler(event, context):
    send_info({"message":"Handling delta..."})
    # if no state info present, nothing we can do
    if 'state' not in event:
        return

    # the delta channel spits back the desired state
    # if desired and reported states differ
    desired_state = event['state']

    update_to_desired_state(desired_state)

    return
