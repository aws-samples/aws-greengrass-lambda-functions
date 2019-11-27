import boto3
import greengrasssdk
import json
import logging
import os
import platform
from botocore.exceptions import ClientError
from threading import Timer

# Create a CloudWatch client
cloudwatch_client = boto3.client('cloudwatch')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

GROUP_ID = os.environ['GROUP_ID']
THING_NAME = os.environ['AWS_IOT_THING_NAME']
THING_ARN = os.environ['AWS_IOT_THING_ARN']
NAMESPACE = os.environ['NAMESPACE']

payload = {}
payload['group_id'] = GROUP_ID
payload['thing_name'] = THING_NAME
payload['thing_arn'] = THING_ARN


# This is a dummy handler and will not be invoked
# Instead the code above will be executed in an infinite loop for our example
def function_handler(event, context):
    for key in event.keys():
        value = event[key]

        if not isinstance(value, list):
            continue

        # We have a list, find elements that are dictionaries that have the expected keys
        for element in value:
            if not isinstance(element, dict):
                continue

            if not 'name' in element.keys():
                continue

            if not 'unit' in element.keys():
                continue

            if not 'value' in element.keys():
                continue

            name = element['name']
            unit = element['unit']
            value = element['value']

            cloudwatch_client.put_metric_data(Namespace=NAMESPACE,
                                              MetricData=[
                                                  {
                                                      'MetricName': name,
                                                      'Value': value,
                                                      'Unit': unit
                                                  }
                                              ])

    return
