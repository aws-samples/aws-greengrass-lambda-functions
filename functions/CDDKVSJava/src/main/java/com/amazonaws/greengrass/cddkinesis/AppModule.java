package com.amazonaws.greengrass.cddkinesis;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@Module
public class AppModule {
    @Provides
    public DefaultCredentialsProvider defaultCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }
}
