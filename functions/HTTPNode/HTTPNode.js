// HTTPNode.js

const ggSdk = require('aws-greengrass-core-sdk')

const iotClient = new ggSdk.IotData()
const os = require('os')
const https = require('https')

const GROUP_ID = process.env.GROUP_ID
const THING_NAME = process.env.AWS_IOT_THING_NAME
const THING_ARN = process.env.AWS_IOT_THING_ARN

const base_topic = THING_NAME + '/http_node'
const request_topic = base_topic + '/request'
const response_topic = base_topic + '/response'

function publishCallback(err, data) {
    console.log(err);
    console.log(data);
}

// This executes the HTTP requests
exports.function_handler = function(event, context) {
    console.log('event: ' + JSON.stringify(event));
    console.log('context: ' + JSON.stringify(context));

    inbound_topic = context['clientContext']['Custom']['subject']

    if (!(inbound_topic.startsWith(request_topic))) {
        console.log("Inbound topic is not the request topic hierarchy %s %s", inbound_topic, request_topic)
        return
    }

    if (!('id' in event)) {
        console.log('No ID found')
        return
    }

    if (!('action' in event)) {
        console.log('No action found')
        return
    }

    if (!('url' in event)) {
        console.log('No url found')
        return
    }

    action = event['action']

    if ((action != 'get') && (action != 'post')) {
        console.log("Only get and post actions are supported %s", action)
        return
    }

    method = action.toUpperCase()

    id = event['id']
    url = event['url']

    port = undefined
    hostname = undefined
    path = '/'

    if (url.startsWith('https://')) {
        port = 443
        url = url.substring('https://'.length)
    }
    else if (url.startsWith('http://')) {
        port = 80
        url = url.substring('http://'.length)
    }

    hostname_end = url.indexOf('/')

    if (hostname_end != -1) {
        hostname = url.substring(0, hostname_end)
	path = url.substring(hostname_end)
    }
    else {
        hostname = url
    }

    port_index = hostname.indexOf(':')

    if (port_index != -1) {
        port = parseInt(hostname.substring(port_index + 1))
        hostname = hostname.substring(0, port_index)
    }

    const options = {
        hostname: hostname,
        port: port,
        path: path,
        method: method
    }

    console.log(JSON.stringify(options))

    const req = https.request(options, (res) => {
        data = ''
        error = ''

        res.on('data', (chunk) => {
            data += chunk
        })

        res.on('end', () => {
            if (data.length > (127 * 1024)) {
                error = 'Data was too large to fit in a single MQTT message'
                data = ''
            }

            const pubOpt = {
                topic: response_topic + '/' + id,
                payload: JSON.stringify({ response: data, error: error })
            };

            iotClient.publish(pubOpt, publishCallback);
        })
    })

    req.on('error', (error) => {
        console.error(error)
    })

    req.end()
};
