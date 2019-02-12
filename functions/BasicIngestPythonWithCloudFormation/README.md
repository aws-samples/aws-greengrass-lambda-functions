# Basic Ingest Python with CloudFormation

## What is this function?

This function is similar to the Hello World functions but rather than publish the Hello World
messages on a topic in IoT Core it publishes them to a reserved topic which routes them to a
rule via basic ingest. The CloudFormation creates the rule used for basic ingest.

The messages from the core look like this:

```json
{
  "message": "Basic ingest received from AWS_GREENGRASS_GROUP_NAME"
}
```

Where `AWS_GREENGRASS_GROUP_NAME` is the name of each group.

The messages after the rule has processed them go on the `greengrass_basic_ingest` topic and
look like this:

```json
{
  "message": "Basic ingest received from AWS_GREENGRASS_GROUP_NAME",
  "processed_by_basic_ingest": "yes"
}
```
