# CDD Embedded Vaadin Skeleton Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This function runs an application built on the Vaadin framework inside of Greengrass.  This
provides a full web interface that can interact with Greengrass Devices, Greengrass Lambda
functions, and local processes.  Since it is running inside of Greengrass it can be accessed even
when there is no Internet connectivity.

This particular application is simply a skeleton that can be used to build new UIs off of.  A timer is fired
every 5 seconds that puts a timestamp in two grids on the web page.  If you publish a message on the
`${AWS_IOT_THING_NAME}/cdd/embeddedvaadinskeleton/input` topic that message will also be placed in the grid.

The data that backs the grid is not persistent and not shared between clients.  The grid resets when the user
refreshes the page.

## How do I connect to it?

Browse to the IP address of your Greengrass Core on the port specified as `PORT` in the
`environmentVariables` section of `function.conf`.
