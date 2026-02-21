package dev.isnow.sixhundredelo;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.process.ProcessUtil;

import java.util.Optional;

public class Entrypoint {
    public static void main(final String[] args) {
        final Optional<Long> optionalPid = ProcessUtil.findPid("cs2.exe");

        if (optionalPid.isEmpty()) {
            throw new RuntimeException("No cs2 instance found.");
        }

        final long pid = optionalPid.get();
        System.out.println("Pid: " + pid);

        final ProcessMemory process = new ProcessMemory(pid);
        Runtime.getRuntime().addShutdownHook(new Thread(process::close));

        new SixhundredElo(process);
    }
}
