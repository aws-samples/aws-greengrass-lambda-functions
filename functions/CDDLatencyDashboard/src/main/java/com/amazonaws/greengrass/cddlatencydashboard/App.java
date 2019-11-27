package com.amazonaws.greengrass.cddlatencydashboard;

import com.amazonaws.greengrass.cddlatencydashboard.modules.AppModule;
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
