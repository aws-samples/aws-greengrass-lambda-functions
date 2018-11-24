package com.timmattison.greengrass.cdd.nativeprocesses;

import com.timmattison.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
public class LinkerHackNativeProcessHelper implements NativeProcessHelper {
    @Getter(lazy = true)
    private final String architecture = determineArchitecture();

    private Optional<String> linker = Optional.empty();

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

    private Optional<Path> findLinker() {
        Optional<Stream.Builder<Path>> fileStreamBuilder = Optional.of(Stream.builder());

        walkFileSystem(log, fileStreamBuilder, Optional.empty(), Optional.empty());

        return findLinker(Optional.of(fileStreamBuilder.get().build()));
    }

    private String getLinker() {
        if (!linker.isPresent()) {
            linker = Optional.of(findLinker().orElseThrow(() -> new UnsupportedOperationException("Couldn't find linker")).toFile().getAbsolutePath());
        }

        return linker.get();
    }

    private Optional<Path> findLinker(Optional<Stream<Path>> files) {
        if (!files.isPresent()) {
            return Optional.empty();
        }

        return files.get()
                .map(Path::toAbsolutePath)
                .distinct()
                .filter(path -> path.toFile().getAbsolutePath().contains("ld-linux"))
                .filter(path -> path.toFile().getAbsolutePath().matches("^.*\\.so\\.[0-9]+$"))
                .sorted()
                .findFirst();
    }
}
