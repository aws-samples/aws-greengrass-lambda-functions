# LiFXPython.py

import json
import logging
import time
from threading import Timer

import greengrasssdk
import lifx
from lifx.color import HSBK

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

base_topic = 'cdd/lifx'
command_topic = base_topic + "/command"
command_status_topic = command_topic + "/status"

lights = None


def greengrass_lifx_stall():
    print 'Doing nothing'


def greengrass_lifx_run():
    logger.info('Starting LiFX client')
    # Create the client and start discovery
    global lights
    lights = lifx.Client()

    # Asynchronously schedule this function to be run again in 5 seconds
    Timer(5, greengrass_lifx_stall).start()


greengrass_lifx_run()


def function_handler(event, context):
    global lights
    inbound_topic = context.client_context.custom['subject']

    if not inbound_topic == command_topic:
        logger.info('Inbound topic is not the command topic %s %s', inbound_topic, command_topic)
        return

    if not ('action' in event.keys()):
        logger.info('No action found')
        return

    action = event['action']

    output = []

    if action == 'scan':
        logger.info('Scan action')
        for l in lights.get_devices():
            logger.info('Found bulb %s', l.label)
            output.append(l.label)

        logger.info('Publishing list')
        client.publish(topic=command_status_topic, payload=json.dumps(output))
        logger.info('List published')
        return

    if not ('bulb' in event.keys()):
        logger.info('No bulb specified')
        return

    bulb = event['bulb']

    if not ((action == 'on') or (action == 'off')):
        logger.info('Invalid action specified %s', action)
        return

    if action == 'on':
        action = True
    else:
        action = False

    if 'color' in event.keys():
        print 'Color set'
        color = event['color']
        [h, s, b, k] = color.split(',')
        color = HSBK(h, s, b, k)
    else:
        print 'No color set'
        color = HSBK(0, 0, 100, 9000)

    for i in range(0, 3):
        for l in lights.get_devices():
            if not l.label == bulb:
                continue

            logger.info('Setting power for bulb %s', bulb)
            l.power = action

            if action == True:
                print 'Set color'
                print color
                l.color = color
            logger.info('Power set for bulb %s', bulb)
            return

        logger.info('Not found, trying again')
        time.sleep(1)

    logger.info('Fell all the way through, bulb name wrong? %s', bulb)
