package com.awslabs.aws.iot.greengrass.cdd.nativeprocesses;

import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class LinkerHackNativeProcessHelper implements NativeProcessHelper {
    private final Logger log = LoggerFactory.getLogger(LinkerHackNativeProcessHelper.class);
    private Optional<String> linker = Optional.empty();
    private Optional<String> architecture = Optional.empty();

    @Inject
    public LinkerHackNativeProcessHelper() {
    }

    @Override
    public void runProgramAndBlock(String program, Optional<List<String>> arguments, Optional<Map<String, String>> environmentVariables, Optional<Consumer<String>> stdoutConsumer, Optional<Consumer<String>> stderrConsumer) {
        if (!program.startsWith("/")) {
            program = String.join("/", LAMBDA_DIRECTORY, getArchitecture(), program);
        }

        // Very clever - https://askubuntu.com/questions/354342/how-can-i-execute-a-file-without-execute-permissions
        List<String> programAndArguments = new ArrayList<>();
        programAndArguments.add(getLinker());
        programAndArguments.add(program);
        arguments.ifPresent(args -> programAndArguments.addAll(args));

        ProcessBuilder pb = new ProcessBuilder(programAndArguments);

        environmentVariables.ifPresent(env -> pb.environment().putAll(env));

        getOutputFromProcess(log, pb, true, stdoutConsumer, stderrConsumer);
    }

    private String getArchitecture() {
        if (!architecture.isPresent()) {
            architecture = Optional.of(determineArchitecture());
        }

        return architecture.get();
    }

    private Optional<Path> findLinker() {
        List<Path> files = listAllFiles();

        if (files.isEmpty()) {
            return Optional.empty();
        }

        return findLinker(files);
    }

    private String getLinker() {
        if (!linker.isPresent()) {
            linker = Optional.of(findLinker().orElseThrow(() -> new UnsupportedOperationException("Couldn't find linker")).toFile().getAbsolutePath());
        }

        return linker.get();
    }

    private Optional<Path> findLinker(List<Path> files) {
        return files.stream()
                .map(Path::toAbsolutePath)
                .distinct()
                .filter(path -> path.toFile().getAbsolutePath().contains("ld-linux"))
                .filter(path -> path.toFile().getAbsolutePath().matches("^.*\\.so\\.[0-9]+$"))
                .sorted()
                .findFirst();
    }
}
