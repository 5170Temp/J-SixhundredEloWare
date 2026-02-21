package dev.isnow.sixhundredelo.sdk.process;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class ProcessUtil {
    public Optional<Long> findPid(final String processName) {
        return ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .filter(p -> matchesName(p, processName))
                .map(ProcessHandle::pid)
                .findFirst();
    }

    private boolean matchesName(final ProcessHandle process, final String name) {
        final Optional<String> command = process.info().command();
        if (command.isEmpty()) {
            return false;
        }

        String executable = command.get();
        executable = executable.substring(executable.lastIndexOf("\\") + 1);

        return executable.equalsIgnoreCase(name) || executable.equalsIgnoreCase(name + ".exe");
    }
}
