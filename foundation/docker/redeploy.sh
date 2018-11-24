#!/usr/bin/env bash

GROUP_ID=$1

if [ -f "/greengrass/config/group-id.txt" ]; then
  GROUP_ID=$(cat /greengrass/config/group-id.txt)
fi

if [ -z "$GROUP_ID" ]; then
  echo "Group ID required";
  exit 1
fi

REGION=$(cat /greengrass/config/region.txt)
THING_NAME=$(cat /greengrass/config/thing-name.txt)
CREDENTIAL_PROVIDER_URL="https://"$(cat /greengrass/config/credential-provider-url.txt)"/"
ROLE_ALIAS_NAME=$(cat /greengrass/config/role-alias-name.txt)
FULL_URL=$CREDENTIAL_PROVIDER_URL"role-aliases/"$ROLE_ALIAS_NAME"/credentials"

# No longer using the AWS IoT Verisign root CA.  If the distro doesn't have certificate authorities installed this command will probably fail
CREDENTIALS=$(curl -s --cert /greengrass/certs/core.crt --key /greengrass/certs/core.key -H "x-amzn-iot-thingname: $THING_NAME" $FULL_URL)

export AWS_DEFAULT_REGION=$REGION
export AWS_ACCESS_KEY_ID=$(jq --raw-output .credentials.accessKeyId <(echo $CREDENTIALS))
export AWS_SECRET_ACCESS_KEY=$(jq --raw-output .credentials.secretAccessKey <(echo $CREDENTIALS))
export AWS_SESSION_TOKEN=$(jq --raw-output .credentials.sessionToken <(echo $CREDENTIALS))

GROUP_VERSION_ID=$(aws greengrass get-group --group-id $GROUP_ID --query LatestVersion --output text)

aws greengrass create-deployment --group-id $GROUP_ID --group-version-id $GROUP_VERSION_ID --deployment-type NewDeployment