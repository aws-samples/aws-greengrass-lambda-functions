package com.timmattison.greengrass.cdd.nativeprocesses;

import com.timmattison.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class TempDirNativeProcessHelper implements NativeProcessHelper {
    @Getter(lazy = true)
    private final String architecture = determineArchitecture();

    @Override
    public void runProgramAndBlock(String program, Optional<List<String>> arguments, Optional<Map<String, String>> environmentVariables, Optional<Consumer<String>> stdoutConsumer, Optional<Consumer<String>> stderrConsumer) {
        String fullProgram = program;

        if (!program.startsWith("/")) {
            fullProgram = String.join("/", LAMBDA_DIRECTORY, getArchitecture(), program);
        }

        File programSource = new File(fullProgram);
        File programDestination = new File(String.join("/", "", "tmp", new File(program).getName()));

        try {
            Files.copy(programSource.toPath(), programDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }

        try {
            Files.setPosixFilePermissions(programDestination.toPath(), getPosixFilePermissions());
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }

        List<String> programAndArguments = new ArrayList<>();
        programAndArguments.add(programDestination.getAbsolutePath());
        arguments.ifPresent(args -> programAndArguments.addAll(args));

        ProcessBuilder pb = new ProcessBuilder(programAndArguments);

        environmentVariables.ifPresent(env -> pb.environment().putAll(env));

        getOutputFromProcess(log, pb, true, stdoutConsumer, stderrConsumer);
    }

    public Set<PosixFilePermission> getPosixFilePermissions() {
        Set<PosixFilePermission> posixFilePermissions = new HashSet<>();

        posixFilePermissions.add(PosixFilePermission.GROUP_EXECUTE);
        posixFilePermissions.add(PosixFilePermission.GROUP_READ);
        posixFilePermissions.add(PosixFilePermission.OWNER_EXECUTE);
        posixFilePermissions.add(PosixFilePermission.OWNER_READ);
        posixFilePermissions.add(PosixFilePermission.OTHERS_EXECUTE);
        posixFilePermissions.add(PosixFilePermission.OTHERS_READ);

        return posixFilePermissions;
    }
}
