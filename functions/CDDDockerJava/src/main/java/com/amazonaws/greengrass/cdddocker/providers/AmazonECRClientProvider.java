package com.amazonaws.greengrass.cdddocker.providers;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.services.ecr.AmazonECRClient;
import com.amazonaws.services.ecr.AmazonECRClientBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

public class AmazonECRClientProvider implements Provider<AmazonECRClient> {
    @Inject
    AWSCredentialsProviderChain awsCredentialsProviderChain;

    @Inject
    public AmazonECRClientProvider() {
    }

    @Override
    public AmazonECRClient get() {
        return (AmazonECRClient) AmazonECRClientBuilder.standard().withCredentials(awsCredentialsProviderChain).build();
    }
}
