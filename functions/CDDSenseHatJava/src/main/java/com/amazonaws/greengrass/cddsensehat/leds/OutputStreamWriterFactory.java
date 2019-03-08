package com.amazonaws.greengrass.cddsensehat.leds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Created by timmatt on 2/24/17.
 */
public interface OutputStreamWriterFactory {
    OutputStreamWriter get(File file, Charset charset) throws FileNotFoundException;
}
