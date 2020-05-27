#!/usr/bin/env bash

JAR_NAME="AwsGreengrassProvisioner.jar"
JAR_LOCATION_1="../aws-greengrass-provisioner/build/libs/$JAR_NAME"
JAR_LOCATION_2="./$JAR_NAME"

if [ -f "$JAR_LOCATION_1" ]; then
  JAR_LOCATION=$JAR_LOCATION_1
elif [ -f "$JAR_LOCATION_2" ]; then
  JAR_LOCATION=$JAR_LOCATION_2
fi

if [ ! -z "$JAR_LOCATION" ]; then
  echo "JAR found, running with Java"
  set -e
  java -jar $JAR_LOCATION $@
  exit 0
fi

echo "JAR not found, running with Docker"
TAG=$(cd ../aws-greengrass-provisioner 2>/dev/null && git symbolic-ref --short HEAD | tr -cd '[:alnum:]._-')

if [ $? -ne 0 ]; then
  TAG=master
else
  if [ -z "$TAG" ]; then
    TAG=$(cd ../aws-greengrass-provisioner 2>/dev/null && git rev-parse HEAD)
  fi

  echo Using aws-greengrass-provisioner repo branch $TAG
fi

AWS_CLI_ERROR_MESSAGE_PREFIX="No"
AWS_CLI_ERROR_MESSAGE_SUFFIX="found via aws configure get, do you have the AWS CLI configured on this system? This command does NOT retrieve credentials from EC2 instance metadata."
AWS_CLI_ERROR_EXIT_CODE=1

# Allow failures, we will catch them
set +e

CLI=1
DOCKER_SESSION_TOKEN=""

hash aws 2>/dev/null

if [ $? -ne 0 ]; then
  echo "AWS CLI not installed, looking for credentials via instance metadata service (EC2 only)"
  CLI=0
else
  # Is the AWS CLI configured?
  AWS_ACCESS_KEY_ID=$(aws configure get aws_access_key_id)
fi

if [ $? -ne 0 ] || [ $CLI -eq 0 ]; then
  # AWS CLI is not configured.  Are we running on an EC2 instance with a role?
  ROLE=$(curl -s --connect-timeout 3 http://169.254.169.254/latest/meta-data/iam/security-credentials/)

  if [ $? -ne 0 ]; then
    echo $AWS_CLI_ERROR_MESSAGE_PREFIX access key ID $AWS_CLI_ERROR_MESSAGE_SUFFIX
    exit $AWS_CLI_ERROR_EXIT_CODE
  else
    if [[ $ROLE == \<* ]]; then
      echo This EC2 instance appears to have no role associated with it. You must assign a role to this EC2 instance or provide environment variables with AWS credentials to use this script.
      exit 3
    fi

    # Looks like an EC2 instance with a role, get the credentials
    CREDENTIALS=$(curl -s http://169.254.169.254/latest/meta-data/iam/security-credentials/$ROLE)

    # Extract the credentials
    AWS_ACCESS_KEY_ID=$(echo $CREDENTIALS | sed -e 's/^.*AccessKeyId\" : \"\([^\"]*\).*/\1/')
    AWS_SECRET_ACCESS_KEY=$(echo $CREDENTIALS | sed -e 's/^.*SecretAccessKey\" : \"\([^\"]*\).*/\1/')
    AWS_SESSION_TOKEN=$(echo $CREDENTIALS | sed -e 's/^.*Token\" : \"\([^\"]*\).*/\1/')
    DOCKER_SESSION_TOKEN="-e AWS_SESSION_TOKEN=$AWS_SESSION_TOKEN"

    # Get the region
    AVAILABILITY_ZONE=$(curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone)
    REGION="$(echo \"$AVAILABILITY_ZONE\" | sed 's/[a-z]$//')"
  fi
else
  AWS_SECRET_ACCESS_KEY=$(aws configure get aws_secret_access_key)

  if [ $? -ne 0 ]; then
    echo $AWS_CLI_ERROR_MESSAGE_PREFIX secret access key $AWS_CLI_ERROR_MESSAGE_SUFFIX
    exit $AWS_CLI_ERROR_EXIT_CODE
  fi

  REGION=$(aws configure get region)

  if [ $? -ne 0 ]; then
    echo $AWS_CLI_ERROR_MESSAGE_PREFIX region $AWS_CLI_ERROR_MESSAGE_SUFFIX
    exit $AWS_CLI_ERROR_EXIT_CODE
  fi
fi

# Simple check to make sure docker is installed
hash docker 2>/dev/null

if [ $? -ne 0 ]; then
  echo "Docker may not be installed on your system. If you received a permission denied error try running this script again as root. Otherwise, please install docker and try again."
  exit 2
fi

# All errors after this point are fatal and are handled by OS error messages
set -e

PWD=$(pwd)

docker pull timmattison/aws-greengrass-provisioner:$TAG

TEMP_CONTAINER=$(docker create timmattison/aws-greengrass-provisioner:$TAG)

docker cp $PWD/foundation $TEMP_CONTAINER:/foundation
docker cp $PWD/deployments $TEMP_CONTAINER:/deployments
docker cp $PWD/functions $TEMP_CONTAINER:/functions
docker cp $PWD/connectors $TEMP_CONTAINER:/connectors

TEMP_IMAGE=$(uuidgen | tr '[:upper:]' '[:lower:]' | tr -d '-')

docker commit $TEMP_CONTAINER $TEMP_IMAGE
docker rm $TEMP_CONTAINER

docker run \
  -v $PWD/credentials:/credentials \
  -v $PWD/build:/build \
  -v $HOME/.ssh:/root/.ssh \
  -v $PWD/dtoutput:/dtoutput \
  -v $PWD/logs:/logs \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -i --rm \
  -e AWS_REGION=$REGION \
  -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
  -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
  $DOCKER_SESSION_TOKEN \
  $TEMP_IMAGE $@
