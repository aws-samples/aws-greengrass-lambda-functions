# Dockerfile README

There are two Dockerfiles in this directory.  One is for building ARM32 Docker containers, the
other is for building X86_64 Docker containers.  Through QEMU magic the ARM32 Docker container
can be built on X86_64.

The script redeploy.sh in this directory is included with the Docker builds to force the most
recent deployment of your Greengrass Core to be redeployed each time you start the container.
This is necessary because these Docker containers do not have any persistent storage.
Without the redeploy script your Greengrass Core will deploy properly the first time but if
you restart it it will have an empty deployment and do nothing.
