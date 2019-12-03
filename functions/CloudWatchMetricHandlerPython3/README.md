# CloudWatch metric handler function (Python 3)

## What is this function?

This function is a Python 3 function that receives event on any topic it is subscribed to. When an event is received it
performs the following process on the top-level keys:
- Is this value for this key an array? If not, go to the next key. If so, continue.
- Loop through all elements in the array:
  - Is this array element an object? If not, go to the next array element. If so, continue.
  - Does this object contain the keys `name`, `unit`, and `value`? If not, go to the next array element. If so, continue.
  - Call put_metric_data with the namespace value in the `NAMESPACE` environment variable, the name from `name`, the units
from `unit`, and the value from `value`

NOTE: This function requires an environment variable `NAMESPACE` to specify the namespace where the CloudWatch metrics should be stored.

NOTE: Any group with this function must have an IAM policy that allows it to put CloudWatch metrics.

## What does the output look like?

This function has no output. There will be output in the logs if anything goes wrong.

## What is an example message it can receive?

```json
{
  "uuid": "b80151f1-ed42-4cf1-9a78-f8e150be4f10",
  "latencies":
  [
    {
      "name": "Default gateway",
      "unit": "Milliseconds",
      "value": 1.9714832305908203
    },
    {
      "name": "AWS Virginia console",
      "unit": "Milliseconds",
      "value": 10.42795181274414
    },
    {
      "name": "AWS Oregon console",
      "unit": "Milliseconds",
      "value": 68.4196949005127
    },
    {
      "name": "AWS Singapore console",
      "unit": "Milliseconds",
      "value": 237.47801780700684
    }
  ]
}
```

In this case the `uuid` field is ignored and four metrics are put into CloudWatch metrics.
