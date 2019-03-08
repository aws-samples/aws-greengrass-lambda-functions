package com.amazonaws.greengrass.cddsensehat;

import com.amazonaws.greengrass.cddsensehat.modules.AppModule;
import com.timmattison.greengrass.cdd.BaselineAppInterface;
import lombok.NoArgsConstructor;

import java.util.Arrays;

// Greengrass requires a no-args constructor
@NoArgsConstructor(force = true)
public class App implements BaselineAppInterface {
    static {
        BaselineAppInterface.initialize(Arrays.asList(new AppModule()));
    }
}
