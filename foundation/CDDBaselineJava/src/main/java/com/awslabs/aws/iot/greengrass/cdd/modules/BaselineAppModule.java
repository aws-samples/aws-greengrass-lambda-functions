package com.awslabs.aws.iot.greengrass.cdd.modules;

import com.amazonaws.greengrass.javasdk.IotDataClient;
import com.amazonaws.greengrass.javasdk.LambdaClient;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.amazonaws.greengrass.javasdk.model.InvokeRequest;
import com.amazonaws.greengrass.javasdk.model.InvokeResponse;
import com.awslabs.aws.iot.greengrass.cdd.communication.*;
import com.awslabs.aws.iot.greengrass.cdd.helpers.JsonHelper;
import com.awslabs.aws.iot.greengrass.cdd.helpers.TimerHelper;
import com.awslabs.aws.iot.greengrass.cdd.helpers.implementations.BasicJsonHelper;
import com.awslabs.aws.iot.greengrass.cdd.helpers.implementations.BasicTimerHelper;
import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.TempDirNativeProcessHelper;
import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import com.awslabs.aws.iot.greengrass.cdd.providers.BasicEnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.DummyEnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.GreengrassSdkErrorHandler;
import com.awslabs.aws.iot.greengrass.cdd.providers.SafeProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import dagger.Module;
import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Module
public class BaselineAppModule {
    private static final Logger log = LoggerFactory.getLogger(BaselineAppModule.class);
    private static Optional<Boolean> optionalRunningInGreengrass = Optional.empty();

    @Provides
    public IotDataClient providesIotDataClient() {
        return new IotDataClient();
    }

    @Provides
    @Singleton
    public Dispatcher providesDispatcher(BasicDispatcher basicDispatcher) {
        return basicDispatcher;
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

    public boolean runningInGreengrass() {
        if (!optionalRunningInGreengrass.isPresent()) {
            try {
                providesIotDataClient();
                optionalRunningInGreengrass = Optional.of(true);
            } catch (NoClassDefFoundError e) {
                if (e.getMessage().contains("EnvVars")) {
                    // Not running in Greengrass
                    optionalRunningInGreengrass = Optional.of(false);
                }

                log.error("Exception while determining if the environment is Greengrass");
                log.error(e.getMessage());
                e.printStackTrace();

                // Assume not running in Greengrass if issues arise
                optionalRunningInGreengrass = Optional.of(false);
            }
        }

        return optionalRunningInGreengrass.get();
    }

    @Provides
    public LambdaClientInterface providesLambdaClientInterface() {
        if (runningInGreengrass()) {
            return new LambdaClientInterface() {
                private LambdaClient lambdaClient = new LambdaClient();

                @Override
                public InvokeResponse invoke(InvokeRequest invokeRequest) throws GGLambdaException {
                    return lambdaClient.invoke(invokeRequest);
                }
            };
        } else {
            return new DummyLambdaClient();
        }
    }

    @Provides
    @Singleton
    public Communication providesCommunication(Provider<GreengrassCommunication> greengrassCommunicationProvider, Provider<DummyCommunication> dummyCommunicationProvider) {
        Communication communication;

        if (runningInGreengrass()) {
            communication = greengrassCommunicationProvider.get();
        } else {
            communication = dummyCommunicationProvider.get();
        }

        return communication;
    }

    // Special environment information (thing name, thing ARN, group name)
    @Provides
    public EnvironmentProvider providesEnvironmentProvider(BasicEnvironmentProvider basicEnvironmentProvider, DummyEnvironmentProvider dummyEnvironmentProvider) {
        if (runningInGreengrass()) {
            return basicEnvironmentProvider;
        }

        return dummyEnvironmentProvider;
    }

    @Provides
    public TimerHelper providesTimerHelper(BasicTimerHelper basicTimerHelper) {
        return basicTimerHelper;
    }
}
