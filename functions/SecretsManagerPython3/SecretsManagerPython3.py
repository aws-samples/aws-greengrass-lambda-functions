import json
import logging
import os
from threading import Timer

import greengrasssdk

# Create a Greengrass IoT data client
client = greengrasssdk.client('iot-data')

# Create a Greengrass secrets manager client
secrets_client = greengrasssdk.client('secretsmanager')

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

# When deployed to a Greengrass core, this code will be executed immediately
# as a long-lived lambda function.  The code will enter the infinite while loop
# below.

GROUP_ID = os.environ['GROUP_ID']
THING_NAME = os.environ['AWS_IOT_THING_NAME']
THING_ARN = os.environ['AWS_IOT_THING_ARN']

payload = {}
payload['group_id'] = GROUP_ID
payload['thing_name'] = THING_NAME
payload['thing_arn'] = THING_ARN
payload['message'] = 'Secrets manager function running'

secret_names = []

# Get the list of secrets this function has access to
for key in os.environ.keys():
    if key.startswith("SECRET_"):
        secret_names.append(os.environ[key])


def greengrass_secrets_manager_run():
    secrets = {}

    for secret_name in secret_names:
        # Build a dictionary that will have either the secret value or the error message
        secrets[secret_name] = {}

        try:
            # Attempt to get the secret value
            response = secrets_client.get_secret_value(SecretId=secret_name)
            secret_value = response.get('SecretString')
            secrets[secret_name]['value'] = secret_value
        except greengrasssdk.SecretsManager.SecretsManagerError:
            secrets[secret_name]['error'] = 'Failed to retrieve secret'

    payload['secrets'] = secrets

    client.publish(topic=THING_NAME + '/python3/secrets/manager', payload=json.dumps(payload))

    # Asynchronously schedule this function to be run again in 5 seconds
    Timer(5, greengrass_secrets_manager_run).start()


# Asynchronously schedule this function to run in 5 seconds. We don't call the function immediately in case the secrets
#   manager configuration is incorrect.
Timer(5, greengrass_secrets_manager_run).start()


# This is a dummy handler and will not be invoked
# Instead the code above will be executed in an infinite loop for our example
def function_handler(event, context):
    return
