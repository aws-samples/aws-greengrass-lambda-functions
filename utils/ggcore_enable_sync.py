import boto3
import logging
import json
import re
import uuid
import argparse

'''
This script enables the IoT shadow syncing for your AWS Greengrass Core. 

Call it with the ID of your Greengrass Group. 

See also https://forums.aws.amazon.com/thread.jspa?messageID=831623
'''

logging.basicConfig(level=logging.INFO)

ap = argparse.ArgumentParser(description='Enables shadow syncing for a Greengrass Core.')
ap.add_argument('group_id', help='Greengrass group id')
args = vars(ap.parse_args())

def call_and_log(method, params):
    logging.info('Calling greengrass.{} with params:\n{}'.format(method, json.dumps(params, indent=4)))
    callable = getattr(client, method)
    result = callable(**params)
    del result['ResponseMetadata']
    if result.get('NextToken') is not None:
        del result['NextToken']
    logging.info('Result of greengrass.{}:\n{}'.format(method, json.dumps(result, indent=4)))
    return result

client = boto3.client('greengrass')

group_id = args['group_id']

# Step 1 - Find the latest group version

params = {
    'GroupId':group_id,
    'MaxResults':'2'
}
result = call_and_log('list_group_versions', params)

group_version_obj = result['Versions'][0]

params = {
    'GroupId':group_id,
    'GroupVersionId':group_version_obj['Version']
}
group_version_obj = call_and_log('get_group_version', params)

# Step 2 - Get the latest core definition version to check shadow syncing

core_definition_version_arn = group_version_obj['Definition']['CoreDefinitionVersionArn']
matches = re.search(r'arn:aws:greengrass:.*:.*:/greengrass/definition/cores/(.*)/versions/(.*)', core_definition_version_arn)
core_definition_id = matches.group(1)
core_definition_version_id = matches.group(2)

params = {
    'CoreDefinitionId': core_definition_id,
    'CoreDefinitionVersionId': core_definition_version_id
}
result = call_and_log('get_core_definition_version', params)
core_definition_version = result['Definition']['Cores'][0]

# Step 3 - Create a new core definition version to enable core shadow syncing

core_definition_version['Id'] = str(uuid.uuid4())
core_definition_version['SyncShadow'] = True    # this is the point of the whole script

params = {
    'CoreDefinitionId': core_definition_id,
    'Cores': [core_definition_version]
}
core_definition_version_new = call_and_log('create_core_definition_version', params)

# Step 4 - Create group definition version with new core definition version

params = group_version_obj['Definition']
params['GroupId'] = group_id
params['CoreDefinitionVersionArn'] = core_definition_version_new['Arn']
result = call_and_log('create_group_version', params)

logging.info('A new version was created for your group, now you can deploy it from the Greengrass console.')
