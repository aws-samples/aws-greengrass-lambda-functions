package com.amazonaws.greengrass.cdddocker.docker;

import com.amazonaws.greengrass.cdddocker.providers.AmazonECRClientProvider;
import com.amazonaws.services.ecr.model.AuthorizationData;
import com.amazonaws.services.ecr.model.GetAuthorizationTokenRequest;
import com.amazonaws.services.ecr.model.GetAuthorizationTokenResult;
import com.amazonaws.util.Base64;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class EcrDockerClientProvider implements DockerClientProvider {
    private static final String SEPARATOR = ":";
    @Inject
    AmazonECRClientProvider amazonECRClientProvider;
    @Inject
    EnvironmentProvider environmentProvider;
    private Optional<AuthorizationData> authorizationData = Optional.empty();
    private long lastUsed = 0;

    @Inject
    public EcrDockerClientProvider() {
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.of(getAuthorizationData().getProxyEndpoint());
    }

    @Override
    public Optional<String> getPassword() {
        String userPassword = getUserPassword();
        return Optional.of(userPassword.substring(userPassword.indexOf(SEPARATOR) + 1));
    }

    @Override
    public Optional<String> getUser() {
        String userPassword = getUserPassword();
        return Optional.of(userPassword.substring(0, userPassword.indexOf(SEPARATOR)));
    }

    @Override
    public EnvironmentProvider getEnvironmentProvider() {
        return environmentProvider;
    }

    private String getUserPassword() {
        return new String(Base64.decode(getAuthorizationData().getAuthorizationToken()));
    }

    private AuthorizationData getAuthorizationData() {
        // This logic makes sure that subsequent calls to getAuthorizationData() don't return different results
        long now = System.nanoTime() / 1000;

        // Is the token more than 60 seconds old?
        if ((now - lastUsed) > 60000) {
            // Yes, clear it out
            authorizationData = Optional.empty();
        }

        lastUsed = System.nanoTime() / 1000;

        if (authorizationData.isPresent()) {
            return authorizationData.get();
        }

        // http://stackoverflow.com/questions/40099527/pulling-image-from-amazon-ecr-using-docker-java
        GetAuthorizationTokenRequest getAuthorizationTokenRequest = new GetAuthorizationTokenRequest();
        GetAuthorizationTokenResult getAuthorizationTokenResult = amazonECRClientProvider.get().getAuthorizationToken(getAuthorizationTokenRequest);
        List<AuthorizationData> authorizationDataList = getAuthorizationTokenResult.getAuthorizationData();
        authorizationData = Optional.of(authorizationDataList.get(0));

        return authorizationData.get();
    }
}
