// WebServerNode.js

const ggSdk = require('greengrass-core-sdk')

const iotClient = new ggSdk.IotData()
const os = require('os')
const express = require('express')

const GROUP_ID = process.env.GROUP_ID
const THING_NAME = process.env.AWS_IOT_THING_NAME
const THING_ARN = process.env.AWS_IOT_THING_ARN
const PORT = process.env.PORT

const base_topic = THING_NAME + '/web_server_node'
const log_topic = base_topic + '/log'

function publishCallback(err, data) {
    console.log(err);
    console.log(data);
}

// This is a handler which does nothing for this example
exports.function_handler = function(event, context) {
    console.log('event: ' + JSON.stringify(event));
    console.log('context: ' + JSON.stringify(context));
};

const app = express()

app.get('/', (req, res) => {
                               res.send('Hello World!')

                               const pubOpt = {
                                   topic: log_topic,
                                   payload: JSON.stringify({ message: 'Hello World request serviced' })
                               };

                               iotClient.publish(pubOpt, publishCallback);
                           }
       )

app.listen(PORT, () => console.log('Example app listening on port ${PORT}!'))
