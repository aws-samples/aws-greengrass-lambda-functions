package com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public interface NativeProcessHelper {
    String BIN_UNAME = "/bin/uname";

    String LAMBDA_DIRECTORY = "/lambda";

    void runProgramAndBlock(String program, Optional<List<String>> arguments, Optional<Map<String, String>> environmentVariables, Optional<Consumer<String>> stdoutLines, Optional<Consumer<String>> stderrLines);

    default List<Path> listAllFiles() {
        Logger log = LoggerFactory.getLogger(NativeProcessHelper.class);
        List<Path> output = new ArrayList<>();

        try {
            Instant startInstant = Instant.now();

            // Adapted from - https://stackoverflow.com/questions/44007055/avoid-java-8-files-walk-termination-cause-of-java-nio-file-accessdeniedexc
            Files.walkFileTree(new File("/").toPath(), new HashSet<>(),
                    Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            // File, add to the list
                            output.add(file);

                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                            // Failed path, ignore it and the subtree (if it is a directory)
                            return FileVisitResult.SKIP_SUBTREE;
                        }

                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            // Directory, add to the list
                            output.add(dir);

                            return FileVisitResult.CONTINUE;
                        }
                    });

            Instant stopInstant = Instant.now();

            Duration duration = Duration.between(startInstant, stopInstant);

            log.debug("Time to walk filesystem: " + duration);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return output;
    }

    default String determineArchitecture() {
        synchronized (this) {
            StringBuilder stdoutStringBuilder = new StringBuilder();
            StringBuilder stderrStringBuilder = new StringBuilder();
            Consumer<String> stdoutConsumer = (string) -> stdoutStringBuilder.append(string);
            Consumer<String> stderrConsumer = (string) -> stderrStringBuilder.append(string);
            runProgramAndBlock(BIN_UNAME, Optional.of(Arrays.asList("-m")), Optional.empty(), Optional.of(stdoutConsumer), Optional.of(stderrConsumer));

            return stdoutStringBuilder.toString();
        }
    }

    default void getOutputFromProcess(Logger logger, ProcessBuilder pb, boolean waitForExit, Optional<Consumer<String>> stdoutConsumer, Optional<Consumer<String>> stderrConsumer) {
        try {
            Process p = pb.start();

            BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            Thread stdoutThread = new Thread(() -> stdout.lines().forEach(stdoutConsumer.orElse(line -> {
            })));
            stdoutThread.start();

            Thread stderrThread = new Thread(() -> stderr.lines().forEach(stderrConsumer.orElse(line -> {
            })));
            stderrThread.start();

            // Did they want to wait for the process to exit?
            if (waitForExit) {
                // Yes, wait for the process to exit
                p.waitFor();

                // Wait for the processing of the STDOUT stream to finish
                stdoutThread.join();

                // Wait for the processing of the STDERR stream to finish
                stderrThread.join();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
