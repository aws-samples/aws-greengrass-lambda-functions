# CDD Skeleton Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This is a skeleton function you can base new functions off of that want to use the CDD framework.
This function will be updated as new features are added to the framework.

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

When the function starts up a message is sent on the `${AWS_IOT_THING_NAME}/cdd/skeleton/output` topic that looks like
this:

```json
{
  "message": "Skeleton started [1515191436573]"
}
```

Every 5 seconds a message is sent on `${AWS_IOT_THING_NAME}/cdd/skeleton/output` topic that looks like this:

```json
{
  "message": "Skeleton still running... [1515191399518]"
}
```