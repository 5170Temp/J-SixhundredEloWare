package dev.isnow.sixhundredelo.sdk;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import lombok.Getter;

@Getter
public class GameAddresses {
    private final long client;
    private final long engineTwo;

    public GameAddresses(final ProcessMemory process) {
        this.client = process.getModuleBase("client.dll").getAddress();
        this.engineTwo = process.getModuleBase("engine2.dll").getAddress();
    }
}
