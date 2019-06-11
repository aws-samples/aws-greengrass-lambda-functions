package com.amazonaws.greengrass.cddsensehat.leds.arrays;

import com.amazonaws.greengrass.cddsensehat.leds.OutputStreamWriterFactory;
import com.amazonaws.greengrass.cddsensehat.leds.SenseHatLEDImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import static java.nio.file.Files.readAllBytes;

public class LinuxSenseHatLEDArray implements SenseHatLEDArray {
    private static final String US_ASCII = "ISO-8859-1";
    private static final Charset CHARSET = Charset.forName(US_ASCII);
    private static final String framebufferDevice = "/dev/fb1";
    private static final File framebufferDeviceFile = new File(framebufferDevice);
    private final Logger log = LoggerFactory.getLogger(LinuxSenseHatLEDArray.class);
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
