#!/usr/bin/env bash

set -e
set -x

# Build the samples
docker build -t arm32samplec .

# Create a container from our image so we can copy the samples out
id=$(docker create arm32samplec)

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
rm -f ARM32SampleC.zip

for sample in $SAMPLES
do
  zip -u ARM32SampleC.zip $sample
  rm $sample
done
