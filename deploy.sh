#!/usr/bin/env bash

AWS_CLI_ERROR_MESSAGE_PREFIX="No"
AWS_CLI_ERROR_MESSAGE_SUFFIX="found via aws configure get, do you have the AWS CLI configured on this system? This command does NOT retrieve credentials from EC2 instance metadata."
AWS_CLI_ERROR_EXIT_CODE=1

set +e

AWS_ACCESS_KEY_ID=$(aws configure get aws_access_key_id)

if [ $? -ne 0 ]; then
  echo $AWS_CLI_ERROR_MESSAGE_PREFIX access key ID $AWS_CLI_ERROR_MESSAGE_SUFFIX
  exit $AWS_CLI_ERROR_EXIT_CODE
fi

AWS_SECRET_ACCESS_KEY=$(aws configure get aws_secret_access_key)

if [ $? -ne 0 ]; then
  echo $AWS_CLI_ERROR_MESSAGE_PREFIX secret access key $AWS_CLI_ERROR_MESSAGE_SUFFIX
  exit $AWS_CLI_ERROR_EXIT_CODE
fi

REGION=$(aws configure get region)

if [ -z "$REGION" ]; then
  echo The REGION variable is not set, cannot continue
  exit 1
fi

set -e

PWD=$(pwd)

docker pull timmattison/aws-greengrass-provisioner:master

docker run \
   -v $PWD/foundation:/foundation \
   -v $PWD/deployments:/deployments \
   -v $PWD/functions:/functions \
   -v $PWD/credentials:/credentials \
   -v $PWD/ggds:/ggds \
   -v $PWD/build:/build \
   -v $HOME/.ssh:/root/.ssh \
   -v /var/run/docker.sock:/var/run/docker.sock \
   -it --rm \
   -e AWS_REGION=$REGION \
   -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
   -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
   timmattison/aws-greengrass-provisioner:master $@
