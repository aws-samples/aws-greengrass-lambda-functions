package com.timmattison.greengrass.cdd.providers.interfaces;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The default implementations in this class are for variables that are provided by the Greengrass environment itself
 */
public interface EnvironmentProvider {
    String ENCODING_TYPE = "ENCODING_TYPE";
    String AWS_IOT_THING_ARN = "AWS_IOT_THING_ARN";
    String AWS_GREENGRASS_GROUP_NAME = "AWS_GREENGRASS_GROUP_NAME";
    String GROUP_ID = "GROUP_ID";

    String PATH = "PATH";
    String AWS_IOT_THING_NAME = "AWS_IOT_THING_NAME";
    String SHADOW_FUNCTION_ARN = "SHADOW_FUNCTION_ARN";
    String ROUTER_FUNCTION_ARN = "ROUTER_FUNCTION_ARN";
    String AWS_GG_HTTP_ENDPOINT = "AWS_GG_HTTP_ENDPOINT";
    String AWS_IOT_HTTP_ENDPOINT = "AWS_IOT_HTTP_ENDPOINT";
    String AWS_GG_MQTT_ENDPOINT = "AWS_GG_MQTT_ENDPOINT";
    String AWS_IOT_MQTT_ENDPOINT = "AWS_IOT_MQTT_ENDPOINT";
    String AWS_CONTAINER_AUTHORIZATION_TOKEN = "AWS_CONTAINER_AUTHORIZATION_TOKEN";
    String GG_MQTT_KEEP_ALIVE = "GG_MQTT_KEEP_ALIVE";
    String MY_FUNCTION_ARN = "MY_FUNCTION_ARN";
    String HOME = "HOME";
    String AWS_CONTAINER_CREDENTIALS_FULL_URI = "AWS_CONTAINER_CREDENTIALS_FULL_URI";
    int ARN_PREFIX_LENGTH = "arn:aws:iot:".length();
    String CDD = "cdd";
    String LOCAL_LAMBDA = "LOCAL_LAMBDA_";

    default Optional<String> get(String name) {
        return Optional.ofNullable(System.getenv(name));
    }

    default Map<String, String> getAll() {
        return System.getenv();
    }

    default Map<String, String> getAllWithPrefix(String prefix) {
        Map<String, String> allEnvironmentVariables = getAll();

        return allEnvironmentVariables.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    default List<String> getLocalLambdaNames() {
        return getAllWithPrefix(LOCAL_LAMBDA).entrySet().stream()
                .map(e -> e.getKey().replaceFirst(LOCAL_LAMBDA, ""))
                .collect(Collectors.toList());
    }

    default Optional<String> getLocalLambdaArn(String localLambdaName) {
        return get(String.join("", LOCAL_LAMBDA, localLambdaName));
    }

    default Optional<String> getRegion() {
        Optional<String> awsIotThingArn = getAwsIotThingArn();

        if (!awsIotThingArn.isPresent()) {
            return Optional.empty();
        }

        int indexOfColonAfterArnPrefix = awsIotThingArn.get().indexOf(':', ARN_PREFIX_LENGTH);

        try {
            return Optional.of(awsIotThingArn.get().substring(ARN_PREFIX_LENGTH, indexOfColonAfterArnPrefix));
        } catch (StringIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    default String getDriverName(Class clazz) {
        Optional<String> output = Arrays.stream(clazz.getTypeName().split("\\."))
                .filter(string -> string.startsWith(CDD))
                .findFirst()
                .map(string -> string.replaceFirst(CDD, ""));

        if (!output.isPresent()) {
            throw new UnsupportedOperationException("Driver package not set up correctly.  You must have one level of your package named \"cddDRIVERNAME\" where DRIVERNAME is the name of your driver (e.g. \"DMI\")");
        }

        return output.get();
    }

    default Optional<String> getPath() {
        return get(PATH);
    }

    default Optional<String> getAwsIotThingName() {
        return get(AWS_IOT_THING_NAME);
    }

    default Optional<String> getShadowFunctionArn() {
        return get(SHADOW_FUNCTION_ARN);
    }

    default Optional<String> getRouterFunctionArn() {
        return get(ROUTER_FUNCTION_ARN);
    }

    default Optional<String> getAwsGgHttpEndpoint() {
        return get(AWS_GG_HTTP_ENDPOINT);
    }

    default Optional<String> getAwsIotHttpEndpoint() {
        return get(AWS_IOT_HTTP_ENDPOINT);
    }

    default Optional<String> getAwsGgMqttEndpoint() {
        return get(AWS_GG_MQTT_ENDPOINT);
    }

    default Optional<String> getAwsIotMqttEndpoint() {
        return get(AWS_IOT_MQTT_ENDPOINT);
    }

    default Optional<String> getAwsContainerAuthorizationToken() {
        return get(AWS_CONTAINER_AUTHORIZATION_TOKEN);
    }

    default Optional<String> getGgMqttKeepAlive() {
        return get(GG_MQTT_KEEP_ALIVE);
    }

    default Optional<String> getMyFunctionArn() {
        return get(MY_FUNCTION_ARN);
    }

    default Optional<String> getHome() {
        return get(HOME);
    }

    default Optional<String> getAwsContainerCredentialsFullUri() {
        return get(AWS_CONTAINER_CREDENTIALS_FULL_URI);
    }

    Optional<String> getAwsIotThingArn();

    Optional<String> getGroupId();
}
