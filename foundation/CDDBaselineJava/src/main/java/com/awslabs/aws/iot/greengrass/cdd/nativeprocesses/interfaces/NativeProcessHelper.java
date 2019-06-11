package com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces;

import org.slf4j.Logger;

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
import java.util.stream.Stream;

public interface NativeProcessHelper {
    String BIN_UNAME = "/bin/uname";

    String LAMBDA_DIRECTORY = "/lambda";

    void runProgramAndBlock(String program, Optional<List<String>> arguments, Optional<Map<String, String>> environmentVariables, Optional<Consumer<String>> stdoutLines, Optional<Consumer<String>> stderrLines);

    default void walkFileSystem(Logger log,
                                Optional<Stream.Builder<Path>> fileStreamBuilder,
                                Optional<Stream.Builder<Path>> directoryStreamBuilder,
                                Optional<Stream.Builder<Path>> failedStreamBuilder) {
        try {
            Instant startInstant = Instant.now();

            // Adapted from - https://stackoverflow.com/questions/44007055/avoid-java-8-files-walk-termination-cause-of-java-nio-file-accessdeniedexc
            Files.walkFileTree(new File("/").toPath(), new HashSet<>(),
                    Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            fileStreamBuilder.ifPresent(builder -> builder.add(file));

                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                            failedStreamBuilder.ifPresent(builder -> builder.add(file));

                            return FileVisitResult.SKIP_SUBTREE;
                        }

                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            directoryStreamBuilder.ifPresent(builder -> builder.add(dir));

                            return FileVisitResult.CONTINUE;
                        }
                    });

            Instant stopInstant = Instant.now();

            Duration duration = Duration.between(startInstant, stopInstant);

            log.debug("Time to walk filesystem: " + duration);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
