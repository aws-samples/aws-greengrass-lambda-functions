package com.amazonaws.greengrass.cddembeddedvaadinskeleton;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.modules.AppModule;
import com.awslabs.aws.iot.greengrass.cdd.BaselineAppInterface;

import java.util.Arrays;

public class App implements BaselineAppInterface {
    static {
        BaselineAppInterface.initialize(Arrays.asList(new AppModule()));
    }

    // Greengrass requires a no-args constructor, do not remove
    public App() {
    }
}
