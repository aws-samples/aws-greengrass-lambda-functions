# Web Server Node

## What is this function?

This is a pinned function that creates a simple web server that listens on the port specified in the
`PORT` environment variable in function.conf. The default port is 8001.

The web server is the [Express "Hello world example"](https://expressjs.com/en/starter/hello-world.html)

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

When the web server receives a request it sends a log message on the `${AWS_IOT_THING_NAME}/web_server_node/log` topic that looks like this:

```json
{
  "message": "Hello World request serviced"
}
```
