#!/usr/bin/env bash

set -e
set -x

# Build the samples
docker build -t x86_64samplec .

# Create a container from our image so we can copy the samples out
id=$(docker create x86_64samplec)

SAMPLES="hello_world_example
invokee
invoker
publish_example
secretsmanager_example
shadow_example
simple_handler"

# Copy out the samples
for sample in $SAMPLES
do
  docker cp $id:/aws-greengrass-core-sdk-c/aws-greengrass-core-sdk-c-example/$sample $sample
done

# Clean up the container
docker rm -v $id

# Zip up the samples
rm -f X86_64SampleC.zip

for sample in $SAMPLES
do
  zip -u X86_64SampleC.zip $sample
  rm $sample
done
