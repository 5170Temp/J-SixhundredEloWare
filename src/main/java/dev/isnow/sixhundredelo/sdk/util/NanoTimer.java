package dev.isnow.sixhundredelo.sdk.util;

import lombok.Getter;

@Getter
public class NanoTimer {
    private long start = 0;
    private boolean running = false;

    public void restart() {
        start = System.nanoTime();
        running = true;
    }

    public void reset() {
        start = 0;
        running = false;
    }

    public long elapsedMs() {
        return running ? (System.nanoTime() - start) / 1_000_000L : 0;
    }
}
