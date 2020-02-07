# CDD KVS Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This is a function that uses GStreamer, Java and Kinesis Video Streams (KVS) to stream a video stream from a Raspberry Pi camera
to KVS. This makes it possible to, for example, use Rekognition to do facial recognition/analysis.

## Requirements

A working Raspberry Pi with KVS producer, for setup these instructions can be used: 
  https://docs.aws.amazon.com/kinesisvideostreams/latest/dg/producersdk-cpp-rpi.html
  
Note: do not use "./install-script", but "./min-install-script". 

Next run "./gstreamer-plugin-install-script" to build the gstreamer plugin.

Note: In this script I had to disable the line that runs "make gstkvsplugintest" to get it working.

## Testing
To test if KVS and GStreamer work, setup the plugin path for gstreamer and try to start a stream.

> export GST_PLUGIN_PATH=<YourKVSSdkFolderPath>/kinesis-video-native-build/downloads/local/lib:$GST_PLUGIN_PATH

> gst-launch-1.0 -v v4l2src do-timestamp=TRUE device=/dev/video0 ! videoconvert 
>! video/x-raw,format=I420,width=640,height=480,framerate=30/1 ! omxh264enc periodicty-idr=45 inline-header=FALSE 
>! h264parse ! video/x-h264,stream-format=avc,alignment=au ! kvssink stream-name=STREAM access-key="ACCESS_KEY" 
>secret-key="SECRET_KEY" aws-region="REGION"

Note: if the camera cannot be used, check that the current user is added to the "video" group on the Pi. Getting the 
camera working is beyond the scope of this example, plenty online resources can be found to get it working.

## Deploying
Using GGP this example can be build and deployed. The configuration should take of setting up the plugin path and 
proper permissions for needed devices.
Check the function.conf file to see if the paths are correct, especially to the location of the GStreamer kinesis plugin.
