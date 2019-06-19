#!/usr/bin/env bash

CORE=$1
IMAGE_NAME=$2

if [ -z "$CORE" ]; then
  echo 'You must specify the full name of the core (including the "_Core" suffix if you used GGP to build this deployment) as the first parameter to this script'
  exit 1
fi

if [ -z "$IMAGE_NAME" ]; then
  echo 'You must specify the name of the image you want to pull (e.g. "hello-world") as the second and final parameter to this script'
  exit 1
fi

aws iot-data publish --topic $CORE/cdd/docker/request --payload "{\"type\":\"PULL\", \"name\":\"$IMAGE_NAME\"}"
