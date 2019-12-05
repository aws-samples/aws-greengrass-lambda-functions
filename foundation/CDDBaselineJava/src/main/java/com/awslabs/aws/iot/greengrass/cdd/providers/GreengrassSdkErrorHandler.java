package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.util.ArrayList;
import java.util.List;

public class GreengrassSdkErrorHandler implements SdkErrorHandler {
    String REGION_EXCEPTION = "Unable to find a region";
    String MISSING_CREDENTIALS_EXCEPTION = "Unable to load AWS credentials from any provider in the chain";
    String BAD_CREDENTIALS_EXCEPTION = "The security token included in the request is invalid";
    String BAD_PERMISSIONS_EXCEPTION = "is not authorized to perform";

    String REGION_ERROR = "Could not determine the AWS region.";
    String REGION_SOLUTION = "Set the AWS_REGION environment variable if the region needs to be explicitly set.";

    String TES_SOLUTION = "Check the error logs to see if TES failed";

    String BAD_CREDENTIALS_ERROR = "The credentials provided may have been deleted or may be invalid.";
    String BAD_CREDENTIALS_SOLUTION = "Make sure the credentials still exist in IAM and that they have permissions to use the services you are requesting.";

    String BAD_PERMISSIONS_SOLUTION = "Add the necessary permissions and try again.";

    @Override
    public Void handleSdkError(SdkClientException e) {
        String message = e.getMessage();
        List<String> errors = new ArrayList<>();

        if (message.contains(REGION_EXCEPTION)) {
            errors.add(REGION_ERROR);
            errors.add(REGION_SOLUTION);
            errors.add(TES_SOLUTION);
        } else if (message.contains(MISSING_CREDENTIALS_EXCEPTION)) {
            errors.add(TES_SOLUTION);
        } else if (message.contains(BAD_CREDENTIALS_EXCEPTION)) {
            errors.add(BAD_CREDENTIALS_ERROR);
            errors.add(BAD_CREDENTIALS_SOLUTION);
            errors.add(TES_SOLUTION);
        } else if (message.contains(BAD_PERMISSIONS_EXCEPTION)) {
            errors.add(message.substring(0, message.indexOf("(")));
            errors.add(BAD_PERMISSIONS_SOLUTION);
            errors.add(TES_SOLUTION);
        }

        if (errors.size() != 0) {
            errors.forEach(System.err::println);
            System.exit(1);
        } else {
            throw e;
        }

        return null;
    }
}
