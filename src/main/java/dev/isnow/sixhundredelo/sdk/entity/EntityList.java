package dev.isnow.sixhundredelo.sdk.entity;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.offset.Offsets;

import java.util.ArrayList;
import java.util.List;

public class EntityList {
    private static final int MAX_PLAYERS = 64;
    private static final int MULTIPLIER = 0x8;
    private static final int ENTRY_OFFSET = 0x10;
    private static final int STRIDE = 0x70;
    private static final int INDEX_MASK = 0x1FF;
    private static final int CROSSHAIR_MASK = 0x7FFF;
    private static final int LIFE_STATE_ALIVE = 256;

    private final ProcessMemory process;
    private final long clientBase;

    public EntityList(final ProcessMemory process, final long clientBase) {
        this.process = process;
        this.clientBase = clientBase;
    }

    public Entity getByIndex(final int rawIndex) {
        final int indexHigh = (rawIndex & CROSSHAIR_MASK) >> 9;
        final int indexLow = rawIndex & INDEX_MASK;

        final long listAddr = getListAddress();
        if (listAddr == 0)
            return null;

        final long entry = process.address(listAddr)
                .offset((long) MULTIPLIER * indexHigh + ENTRY_OFFSET)
                .readLong();
        if (entry == 0)
            return null;

        final long entityAddr = process.address(entry)
                .offset((long) STRIDE * indexLow)
                .readLong();
        if (entityAddr == 0)
            return null;

        return new Entity(process, entityAddr);
    }

    public List<Entity> getAllPlayers() {
        final List<Entity> players = new ArrayList<>();

        final long listAddr = getListAddress();
        if (listAddr == 0)
            return players;

        final long listEntry = process.address(listAddr).offset(ENTRY_OFFSET).readLong();
        if (listEntry == 0)
            return players;

        for (int i = 0; i < MAX_PLAYERS; i++) {
            final long controller = process.address(listEntry).offset((long) i * STRIDE).readLong();
            if (controller == 0)
                continue;

            final int pawnHandle = process.address(controller).offset(Offsets.m_hPlayerPawn).readInt();
            if (pawnHandle == 0)
                continue;

            final long entry2 = process.address(listAddr)
                    .offset((long) MULTIPLIER * ((pawnHandle & CROSSHAIR_MASK) >> 9) + ENTRY_OFFSET)
                    .readLong();
            if (entry2 == 0)
                continue;

            final long pawnAddr = process.address(entry2)
                    .offset((long) STRIDE * (pawnHandle & INDEX_MASK))
                    .readLong();
            if (pawnAddr == 0)
                continue;

            final int lifeState = process.address(pawnAddr).offset(Offsets.m_lifeState).readInt();
            if (lifeState != LIFE_STATE_ALIVE)
                continue;

            players.add(new Entity(process, pawnAddr));
        }

        return players;
    }

    private long getListAddress() {
        return process.address(clientBase).offset(Offsets.dwEntityList).readLong();
    }
}
