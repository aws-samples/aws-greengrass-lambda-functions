#!/usr/bin/env bash

CORE_NAME=$1

if [ -z "$CORE_NAME" ]; then
  echo "You must specify the core name"
  exit 1
fi

echo "Sending invocation request for docker info to ${CORE_NAME}/docker/invoke/list"
aws iot-data publish --topic "${CORE_NAME}/docker/invoke/list" --payload '{"id":"1", "action":"get", "data":"thisisarandomstring"}'