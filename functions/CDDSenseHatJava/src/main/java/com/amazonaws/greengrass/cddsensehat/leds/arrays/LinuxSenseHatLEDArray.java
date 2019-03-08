package com.amazonaws.greengrass.cddsensehat.leds.arrays;

import com.amazonaws.greengrass.cddsensehat.leds.OutputStreamWriterFactory;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import static java.nio.file.Files.readAllBytes;

/**
 * Created by timmatt on 2/20/17.
 */
@Slf4j
public class LinuxSenseHatLEDArray implements SenseHatLEDArray {
    public static final String US_ASCII = "ISO-8859-1";
    public static final Charset CHARSET = Charset.forName(US_ASCII);
    private static final String framebufferDevice = "/dev/fb1";
    private static final File framebufferDeviceFile = new File(framebufferDevice);
    private final OutputStreamWriterFactory outputStreamWriterFactory;

    @Inject
    public LinuxSenseHatLEDArray(OutputStreamWriterFactory outputStreamWriterFactory) {
        this.outputStreamWriterFactory = outputStreamWriterFactory;
    }

    @Override
    public SenseHatLEDImage get() throws IOException {
        return new SenseHatLEDImage(new String(readAllBytes(framebufferDeviceFile.toPath()), CHARSET));
    }

    @Override
    public void put(SenseHatLEDImage senseHatLEDImage) throws IOException {
        OutputStreamWriter writer = outputStreamWriterFactory.get(framebufferDeviceFile, CHARSET);

        writer.write(senseHatLEDImage.renderToString());
        writer.close();
    }
}
