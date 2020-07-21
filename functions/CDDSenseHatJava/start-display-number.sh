#!/usr/bin/env bash

CORE=$1

if [ -z "$CORE" ]; then
  echo 'You must specify the full name of the core (including the "_Core" suffix if you used GGP to build this deployment) as the first and only parameter to this script'
  exit 1
fi

aws iot-data publish --topic $CORE/cdd/sensehat/animation/start/DisplayNumber --cli-binary-format raw-in-base64-out --payload '{}'
