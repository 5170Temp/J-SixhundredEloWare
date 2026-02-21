package dev.isnow.sixhundredelo.sdk;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.entity.EntityList;
import dev.isnow.sixhundredelo.sdk.entity.LocalPlayer;
import dev.isnow.sixhundredelo.sdk.offset.Offsets;
import lombok.Getter;

@Getter
public class GameContext {
    private final ProcessMemory process;
    private final GameAddresses addresses;
    private final EntityList entityList;

    public GameContext(final ProcessMemory process, final GameAddresses addresses) {
        this.process = process;
        this.addresses = addresses;
        this.entityList = new EntityList(process, addresses.getClient());
    }

    public LocalPlayer getLocalPlayer() {
        final long pawn = process.address(addresses.getClient())
                .offset(Offsets.dwLocalPlayerPawn)
                .readLong();

        if (pawn == 0)
            return null;
        return new LocalPlayer(process, pawn, addresses.getClient());
    }
}
