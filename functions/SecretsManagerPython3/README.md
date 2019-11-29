# Secrets Manager Python 3

## What is this function?

This function is a Python 3 function that shows how to use Secrets Manager on Greengrass.

NOTE: To use this function you'll need to deploy a secret to secrets manager called "test-secret". There is a sample CloudFormation template that can be deployed manually (it is not deployed by GGP yet) that will create this test secret. To deploy the template run `deploy-test-secret-stack.sh` to delete the template run `delete-test-secret-stack.sh`.

NOTE: If the secret in the configuration is deleted the group will not be able to be deployed again until the secret is removed from the configuration or it has been recreated.

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

Every 5 seconds a message is sent on `${AWS_IOT_THING_NAME}/python3/secrets/manager` topic that looks like this:

```
{
    "group_id": "93f9d28f-ed45-44ce-979f-5e1d7417afb8",
    "message": "Secrets manager function running",
    "secrets": {
        "test-secret": {
            "value": "This is my test secret!"
        }
    },
    "thing_arn": "arn:aws:iot:us-east-1:5xxxxxxxxxx7:thing/my_Core",
    "thing_name": "my_Core"
}
```

If you change the secret value it will only be updated on the core when the group is redeployed.
