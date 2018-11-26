package com.amazonaws.greengrass.cdddmi.dmi;

import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BasicDmiFetcher implements DmiFetcher {
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String IO_EXCEPTION = "IO_EXCEPTION";
    public static final String ERROR = "ERROR";
    private static final String DMI_BASE_PATH_NAME = "/sys/class/dmi/id";
    private static final Path DMI_BASE_PATH = new File(DMI_BASE_PATH_NAME).toPath();

    @Override
    public Optional<Map<String, Object>> fetch() {
        Map<String, Object> output = new HashMap<>();

        if (!DMI_BASE_PATH.toFile().exists()) {
            output.put(ERROR, "DMI path [" + DMI_BASE_PATH_NAME + "] does not exist");
            return Optional.ofNullable(output);
        }

        try {
            Stream<Path> files = Files.list(DMI_BASE_PATH).filter(path -> !path.toFile().isDirectory());

            // TODO - Recurse into directories below this?  Often these references are circular so we need to be careful.
            // Stream<Path> directories = Files.list(DMI_BASE_PATH).filter(path -> path.toFile().isDirectory());

            List<String> accessDeniedFiles = new ArrayList<>();
            List<String> ioExceptionFiles = new ArrayList<>();

            files.forEach(path -> {
                String name = path.toFile().getName();

                try {
                    output.put(name, new String(Files.readAllBytes(path)));
                } catch (AccessDeniedException e) {
                    e.printStackTrace();
                    accessDeniedFiles.add(name);
                } catch (IOException e) {
                    e.printStackTrace();
                    ioExceptionFiles.add(name);
                }
            });

            if (!accessDeniedFiles.isEmpty()) {
                output.put(ACCESS_DENIED, accessDeniedFiles);
            }

            if (!ioExceptionFiles.isEmpty()) {
                output.put(IO_EXCEPTION, ioExceptionFiles);
            }
        } catch (Exception e) {
            String message = "Failed to get files from DMI directory [" + e.getMessage() + "], has the function been given access to sysfs?";
            log.error(message);
            output.put(ERROR, message);
        }

        return Optional.ofNullable(output);
    }
}
