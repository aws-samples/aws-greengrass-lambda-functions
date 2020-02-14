# CDD DMI Java

## What is CDD?

Cloud Device Driver (CDD) is a framework that makes developers more efficient when writing Lambda
code for Greengrass Cores.  See the `CDDBaseline` README for more information.

## What is this function?

This function is used to obtain information from the Desktop Management Interface (DMI) framework
in Linux via Java on a Greengrass Core.  This information can be used to determine what hardware
a Greengrass Core is running on for the purposes of revenue tracking, compatibility testing, and
error reporting.

If you send any JSON message on the topic `${AWS_IOT_THING_NAME}/cdd/dmi/input` you will get a
message back on the `${AWS_IOT_THING_NAME}/cdd/dmi/output` that contains the DMI information the
function can find.  `${AWS_IOT_THING_NAME}` is a variable that is the name of the Greengrass Core
thing you want to query.  For example, if your Greengrass Core name is `Pi3_Core` the topics
would be `Pi3_Core/cdd/dmi/input` and `Pi3_Core/cdd/dmi/output`.

## What does the output look like?

Here is a sample record from a Raspberry Pi 3 where DMI information isn't available:

```json
{
  "ERROR": "DMI path [/sys/class/dmi/id] does not exist"
}
```

Here is a sample record from a Logic Supply MC850-50 where some DMI information is available:

```json
{
  "product_version": "To Be Filled By O.E.M.\n",
  "chassis_type": "3\n",
  "uevent": "MODALIAS=dmi:bvnAmericanMegatrendsInc.:bvrP1.50A:bd08/25/2017:svnToBeFilledByO.E.M.:pnToBeFilledByO.E.M.:pvrToBeFilledByO.E.M.:rvnASRock:rnIMB-194-L:rvr:cvnToBeFilledByO.E.M.:ct3:cvrToBeFilledByO.E.M.:\n",
  "bios_date": "08/25/2017\n",
  "chassis_asset_tag": "To Be Filled By O.E.M.\n",
  "modalias": "dmi:bvnAmericanMegatrendsInc.:bvrP1.50A:bd08/25/2017:svnToBeFilledByO.E.M.:pnToBeFilledByO.E.M.:pvrToBeFilledByO.E.M.:rvnASRock:rnIMB-194-L:rvr:cvnToBeFilledByO.E.M.:ct3:cvrToBeFilledByO.E.M.:\n",
  "product_name": "To Be Filled By O.E.M.\n",
  "bios_version": "P1.50A\n",
  "board_name": "IMB-194-L\n",
  "chassis_vendor": "To Be Filled By O.E.M.\n",
  "bios_vendor": "American Megatrends Inc.\n",
  "ACCESS_DENIED": [
    "product_uuid",
    "chassis_serial",
    "product_serial",
    "board_serial"
  ],
  "board_version": "        \n",
  "sys_vendor": "To Be Filled By O.E.M.\n",
  "board_asset_tag": "        \n",
  "chassis_version": "To Be Filled By O.E.M.\n",
  "board_vendor": "ASRock\n"
}
```

And this is a sample from an EC2 instance running Ubuntu:

```json
{
  "product_family": "\n",
  "product_version": "4.2.amazon\n",
  "chassis_type": "1\n",
  "uevent": "MODALIAS=dmi:bvnXen:bvr4.2.amazon:bd08/24/2006:svnXen:pnHVMdomU:pvr4.2.amazon:cvnXen:ct1:cvr:\n",
  "chassis_asset_tag": "\n",
  "bios_date": "08/24/2006\n",
  "modalias": "dmi:bvnXen:bvr4.2.amazon:bd08/24/2006:svnXen:pnHVMdomU:pvr4.2.amazon:cvnXen:ct1:cvr:\n",
  "bios_version": "4.2.amazon\n",
  "product_name": "HVM domU\n",
  "bios_vendor": "Xen\n",
  "chassis_vendor": "Xen\n",
  "ACCESS_DENIED": [
    "product_serial",
    "chassis_serial",
    "product_uuid"
  ],
  "sys_vendor": "Xen\n",
  "chassis_version": "\n"
}
```

## Is there a quick way to see if it is working?

Yes. Use the `request.sh` convenience script.

## request.sh usage example

```
./request.sh ${AWS_IOT_THING_NAME}
```

The response on the response topic will contain JSON objects described above
