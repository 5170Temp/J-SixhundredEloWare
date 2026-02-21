package dev.isnow.sixhundredelo.sdk.process.address;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.process.buffer.MemoryBuffer;

public class PointerChain {
    private final ProcessMemory process;
    private final long base;
    private final long[] offsets;

    public PointerChain(final ProcessMemory process, final long base, final long... offsets) {
        this.process = process;
        this.base = base;
        this.offsets = offsets;
    }

    public MemoryAddress resolve() {
        long addr = base;

        for (long offset : offsets) {
            addr = MemoryBuffer.toLong(
                    process.read(addr + offset, 8)
            );
        }

        return process.address(addr);
    }

}