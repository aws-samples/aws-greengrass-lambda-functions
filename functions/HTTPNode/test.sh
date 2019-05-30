#!/usr/bin/env bash

GROUP_NAME=$1

if [ -z "$GROUP_NAME" ]; then
  echo "You must specify the group name"
  exit 1
fi

echo "Sending request for page that exceeds 128k on ID 1"
aws iot-data publish --topic $GROUP_NAME/http_node/request --payload '{"id":"1", "action":"get", "url":"https://aws.amazon.com"}'
echo "Sending request for page that does not exceed 128k on ID 2"
aws iot-data publish --topic $GROUP_NAME/http_node/request --payload '{"id":"2", "action":"get", "url":"https://aws.amazon.com/iot/"}'
