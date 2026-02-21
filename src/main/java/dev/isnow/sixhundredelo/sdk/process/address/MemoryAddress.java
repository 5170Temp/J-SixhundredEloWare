package dev.isnow.sixhundredelo.sdk.process.address;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.process.buffer.MemoryBuffer;
import dev.isnow.sixhundredelo.sdk.math.Vec3;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

@Getter
public class MemoryAddress {
    private final ProcessMemory process;
    private final long address;

    public MemoryAddress(final ProcessMemory process, final long address) {
        this.process = process;
        this.address = address;
    }

    public int readInt() {
        return MemoryBuffer.toInt(process.read(address, 4));
    }

    public float readFloat() {
        return MemoryBuffer.toFloat(process.read(address, 4));
    }

    public long readLong() {
        return MemoryBuffer.toLong(process.read(address, 8));
    }

    public boolean readBool() {
        return process.read(address, 1)[0] != 0;
    }

    public Vec3 readVec3() {
        final byte[] data = process.read(address, 12);
        final ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        return new Vec3(buf.getFloat(), buf.getFloat(), buf.getFloat());
    }

    public float[] readMatrix(final int size) {
        final byte[] data = process.read(address, size * 4);
        final ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        final float[] matrix = new float[size];
        for (int i = 0; i < size; i++) {
            matrix[i] = buf.getFloat();
        }
        return matrix;
    }

    public String readString(final int maxLength) {
        final byte[] data = process.read(address, maxLength);
        int len = 0;
        while (len < data.length && data[len] != 0)
            len++;
        return new String(data, 0, len, StandardCharsets.UTF_8);
    }

    public byte[] readBytes(final int size) {
        return process.read(address, size);
    }

    public void writeInt(final int value) {
        process.write(address, MemoryBuffer.fromInt(value));
    }

    public void writeFloat(final float value) {
        process.write(address, MemoryBuffer.fromFloat(value));
    }

    public void writeLong(final long value) {
        process.write(address, MemoryBuffer.fromLong(value));
    }

    public void writeBytes(final byte[] bytes) {
        process.write(address, bytes);
    }

    public MemoryAddress offset(final long offset) {
        return new MemoryAddress(process, address + offset);
    }
}