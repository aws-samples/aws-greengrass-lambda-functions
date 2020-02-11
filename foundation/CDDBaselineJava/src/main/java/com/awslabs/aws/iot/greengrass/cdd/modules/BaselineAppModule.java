package com.awslabs.aws.iot.greengrass.cdd.modules;

import com.amazonaws.greengrass.javasdk.IotDataClient;
import com.amazonaws.greengrass.javasdk.LambdaClient;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.communication.DummyCommunication;
import com.awslabs.aws.iot.greengrass.cdd.communication.GreengrassCommunication;
import com.awslabs.aws.iot.greengrass.cdd.helpers.JsonHelper;
import com.awslabs.aws.iot.greengrass.cdd.helpers.implementations.BasicJsonHelper;
import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.TempDirNativeProcessHelper;
import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import com.awslabs.aws.iot.greengrass.cdd.providers.BasicEnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.DummyEnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.GreengrassSdkErrorHandler;
import com.awslabs.aws.iot.greengrass.cdd.providers.SafeProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import com.google.common.eventbus.EventBus;
import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

import javax.inject.Singleton;

@Module
public class BaselineAppModule {
    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return new EventBus();
    }

    // Methods to help users launch native processes
    @Provides
    public NativeProcessHelper providesNativeProcessHelper(TempDirNativeProcessHelper tempDirNativeProcessHelper) {
        return tempDirNativeProcessHelper;
    }

    // Error handler to help debugging TES and SDK issues
    @Provides
    public SdkErrorHandler providesSdkErrorHandler(GreengrassSdkErrorHandler greengrassSdkErrorHandler) {
        return greengrassSdkErrorHandler;
    }

    // Use a default credentials provider
    @Provides
    public AwsCredentialsProvider provideAwsCredentialsProvider() {
        return new SafeProvider<AwsCredentialsProvider>(DefaultCredentialsProvider::create).get();
    }

    // JSON helper that auto-wires Immutables type adapters
    @Provides
    public JsonHelper providesJsonHelper(BasicJsonHelper basicJsonHelper) {
        return basicJsonHelper;
    }

    private boolean runningInGreegrass() {
        try {
            new IotDataClient();
        } catch (NoClassDefFoundError e) {
            if (e.getMessage().contains("EnvVars")) {
                // Not running in Greengrass
                return false;
            }
        }

        return true;
    }

    @Provides
    public LambdaClient providesLambdaClient() {
        return new LambdaClient();
    }

    @Provides
    @Singleton
    public Communication providesCommunication(EnvironmentProvider environmentProvider, LambdaClient lambdaClient, EventBus eventBus) {
        if (runningInGreegrass()) {
            return new GreengrassCommunication(environmentProvider, lambdaClient, new IotDataClient(), eventBus);
        }

        return new DummyCommunication(eventBus);
    }

    // Special environment information (thing name, thing ARN, group name)
    @Provides
    public EnvironmentProvider providesEnvironmentProvider(BasicEnvironmentProvider basicEnvironmentProvider, DummyEnvironmentProvider dummyEnvironmentProvider) {
        if (runningInGreegrass()) {
            return basicEnvironmentProvider;
        }

        return dummyEnvironmentProvider;
    }
}
