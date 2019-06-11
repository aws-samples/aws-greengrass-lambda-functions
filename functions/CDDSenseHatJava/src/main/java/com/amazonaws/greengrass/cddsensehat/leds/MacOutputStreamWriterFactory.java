package com.amazonaws.greengrass.cddsensehat.leds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class MacOutputStreamWriterFactory implements OutputStreamWriterFactory {
    private final Logger log = LoggerFactory.getLogger(MacOutputStreamWriterFactory.class);

    @Override
    public OutputStreamWriter get(File file, Charset charset) {
        return new OutputStreamWriter(new OutputStream() {
            private int byteCount = 0;
            private int temp = 0;
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void write(int b) {
                temp |= b;
                byteCount++;

                if ((byteCount % 2) != 0) {
                    temp <<= 8;
                    return;
                }

                if (temp == 0) {
                    stringBuilder.append("_");
                } else {
                    stringBuilder.append("X");
                }

                temp = 0;

                if ((byteCount % 16) == 0) {
                    int line = byteCount / 16;

                    if (line == 1) {
                        log.info("");
                        log.info("");
                    }

                    log.info("Line [" + line + "] " + stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
            }
        });
    }
}
