# SocketServerPython.py

import logging
import os
import socket
from threading import Timer

import greengrasssdk

# Creating a greengrass core sdk client
client = greengrasssdk.client('iot-data')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

base_topic = os.environ['AWS_IOT_THING_NAME'] + '/socketserver'
output_topic = base_topic + '/output'

PORT = int(os.environ['PORT'])


def greengrass_socket_server_run():
    global PORT

    logger.info('Starting socket server')

    HOST = ''

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((HOST, PORT))
    s.listen(1)

    while True:
        conn, addr = s.accept()
        logger.info('Connected by ' + str(addr))
        data = b''

        while 1:
            temp = conn.recv(1024)
            if not temp: break
            data += temp

        conn.close()

        client.publish(topic=output_topic, payload=data)


def function_handler(event, context):
    # Do nothing
    logger.info('Doing nothing with inbound message')


# Asynchronously schedule this function to be run again in 5 seconds
Timer(5, greengrass_socket_server_run).start()
