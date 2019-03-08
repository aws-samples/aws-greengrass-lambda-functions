package com.amazonaws.greengrass.cddsensehat.leds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Created by timmatt on 2/24/17.
 */
public class LinuxOutputStreamWriterFactory implements OutputStreamWriterFactory {
    @Override
    public OutputStreamWriter get(File file, Charset charset) throws FileNotFoundException {
        return new OutputStreamWriter(new FileOutputStream(file), charset);
    }
}
