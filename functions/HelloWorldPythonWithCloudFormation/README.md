# Hello World Python with CloudFormation

## What is this function?

This function is the same as the other Python Hello World function in this repository but it also
launches a CloudFormation template that republishes messages from a core onto a shared topic
`hello/world`.  If you launch this function on multiple cores you should expect to see messages
showing up on `hello/world` for each core.

The messages look like this:

```json
{
  "message": "Hello World received from AWS_GREENGRASS_GROUP_NAME"
}
```

Where `AWS_GREENGRASS_GROUP_NAME` is the name of each group.
