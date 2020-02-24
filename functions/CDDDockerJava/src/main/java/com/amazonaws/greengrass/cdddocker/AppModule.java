package com.amazonaws.greengrass.cdddocker;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.greengrass.cdddocker.docker.BasicDockerHelper;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.amazonaws.greengrass.cdddocker.handlers.BasicLoggingHelper;
import com.amazonaws.greengrass.cdddocker.handlers.LoggingHelper;
import com.amazonaws.services.ecr.AmazonECRClient;
import com.amazonaws.services.ecr.AmazonECRClientBuilder;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    public DockerHelper provideDockerHelper(BasicDockerHelper basicDockerHelper) {
        return basicDockerHelper;
    }

    @Provides
    public AWSCredentialsProviderChain provideAwsCredentialsProviderChain() {
        // This will use the default credential provider chain which should use TES and get temporary credentials for the Greengrass Core's role
        return new DefaultAWSCredentialsProviderChain();
    }

    @Provides
    public AmazonECRClient provideAmazonECRClient(AWSCredentialsProviderChain awsCredentialsProviderChain) {
        return (AmazonECRClient) AmazonECRClientBuilder.standard().withCredentials(awsCredentialsProviderChain).build();
    }

    @Provides
    public LoggingHelper provideLoggingHelper(BasicLoggingHelper basicLoggingHelper) {
        return basicLoggingHelper;
    }
}
