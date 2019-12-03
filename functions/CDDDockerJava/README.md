# CDD Docker Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This is a function that allows you to send commands to the Docker daemon running on a Greengrass host.  It allows
you to start and stop containers, list the current running containers, and pull containers from ECR using the role
associated with your Greengrass Core.

## Is there a quick way to see if it is working?

Yes. There are several convenience scripts you can use to see if the function is working for you.

- `list.sh` - Lists available images and currently running containers
- `pull.sh` - Pulls a container from ECR or a public registry
- `run.sh` - Runs a container by name (repo tag)
- `stop.sh` - Stops a container by name (repo tag) or ID

These scripts interact with the Greengrass core via MQTT. To receive the responses an MQTT client will need to be connected
and subscribed to the `${AWS_IOT_THING_NAME}/cdd/docker/response` topic. All requests are sent on the
`${AWS_IOT_THING_NAME}/cdd/docker/request` topic.

## list.sh usage example

```
./list.sh ${AWS_IOT_THING_NAME}
```

The response on the response topic will contain JSON objects with the details of all locally available images and running containers 

## pull.sh usage examples

Pull the latest version of a public Docker image (e.g. arm32v7/hello-world) with no authentication

```
./pull.sh ${AWS_IOT_THING_NAME} arm32v7/hello-world
```

Pull a specific version of a public Docker image (e.g. arm32v7/hello-world:linux) with no authentication

```
./pull.sh ${AWS_IOT_THING_NAME} arm32v7/hello-world:linux
```

Pull a Docker image from ECR (e.g. repository/image) with the Greengrass core's role

Replace `ACCOUNT_ID`, `REGION`, `repository` and `image` with the appropriate values

```
./pull.sh ${AWS_IOT_THING_NAME} ACCOUNT_ID.dkr.ecr.REGION.amazonaws.com/repository:image
```

## run.sh usage examples

Run a specific version of a public Docker image (e.g. arm32v7/hello-world:latest)

```
./run.sh ${AWS_IOT_THING_NAME} arm32v7/hello-world:latest
```

Run an ECR image

Replace `ACCOUNT_ID`, `REGION`, `repository` and `image` with the appropriate values

```
./run.sh ${AWS_IOT_THING_NAME} ACCOUNT_ID.dkr.ecr.REGION.amazonaws.com/repository:image
```

## stop.sh usage examples

Stop a container by its full ID (only exact matches are supported)

```
./stop.sh ${AWS_IOT_THING_NAME} 6b670c2e8cafcfb0546b4bcab6527cbaa2a8756ff94b7b449e846f0f1525f886
```

Stop a container by tag name (only one container will be stopped if there are multiple containers running from the same tag):

Replace `ACCOUNT_ID`, `REGION`, `repository` and `image` with the appropriate values

```
./stop.sh ${AWS_IOT_THING_NAME} ACCOUNT_ID.dkr.ecr.REGION.amazonaws.com/repository:image
```
