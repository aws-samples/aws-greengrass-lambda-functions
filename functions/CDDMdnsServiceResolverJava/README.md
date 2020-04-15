# CDD MDNS Service Resolver

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This is a function that will emit a message when a new MDNS service has been added, removed, or announced by a broadcast to the local network and resolved by the JmDNS library.

## What does the output look like?

`${AWS_IOT_THING_NAME}` is the name of the thing associated with your Core.

When a service is added a message is sent on the `${AWS_IOT_THING_NAME}/cdd/mdnsserviceresolver/added` topic that looks like this:

```json
{
  "domain": "local",
  "protocol": "tcp",
  "application": "http",
  "name": "UniFi Controller (unifi_yourdomain_com)",
  "inetAddresses": []
}
```

When a service is removed a message is sent on the `${AWS_IOT_THING_NAME}/cdd/mdnsserviceresolver/removed` topic that looks like this:

```json
{
  "domain": "local",
  "protocol": "tcp",
  "application": "http",
  "name": "UniFi Controller (unifi_yourdomain_com)",
  "inetAddresses": []
}
```

When a service is resolved a message is sent on the `${AWS_IOT_THING_NAME}/cdd/mdnsserviceresolver/resolved` topic that looks like this:

```json
{
  "domain": "local",
  "protocol": "tcp",
  "application": "http",
  "name": "UniFi Controller (unifi_yourdomain_com)",
  "inetAddresses": [
    "192.168.1.253"
  ],
  "port": 8080
}
```
