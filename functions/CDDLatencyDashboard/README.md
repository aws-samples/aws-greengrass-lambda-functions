# Latency dashboard

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This function runs an application built on the Vaadin framework inside of Greengrass. This provides a web interface that
shows a simple grid that formats messages about latency between the Greengrass core and other hosts. It does this by
receiving messages in a specific format on any topic.

The data that backs the grid is not persistent and not shared between clients.  The grid resets when the user
refreshes the page.

## How do I connect to it?

Browse to the IP address of your Greengrass Core on the port specified as `PORT` in the
`environmentVariables` section of `function.conf`.

## What should the input data look like?

```json
{
     "latencies": [
         {
             "name": "Default gateway",
             "unit": "Milliseconds",
             "value": 1.2307167053222656
         },
         {
             "name": "AWS Virginia console",
             "unit": "Milliseconds",
             "value": 9.971141815185547
         },
         {
             "name": "AWS Oregon console",
             "unit": "Milliseconds",
             "value": 66.96724891662598
         },
         {
             "name": "AWS Singapore console",
             "unit": "Milliseconds",
             "value": 238.3718490600586
         }
     ],
     "uuid": "11635c42-a7ec-4f4b-b5b6-8f39932e039c"
 }
```

In this case the `uuid` field is ignored and four metrics are displayed on the dashboard.

NOTE: The dashboard is dependent on the latency values always being in the same order. When it receives the first message
it will populate the headers of the grid with the values in the `name` field. If the order of these fields changes the
values will be put in the wrong columns.
