package dev.isnow.sixhundredelo.sdk.process.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MemoryBuffer {
    private static final ByteOrder ORDER = ByteOrder.LITTLE_ENDIAN;

    public static int toInt(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ORDER).getInt();
    }

    public static long toLong(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ORDER).getLong();
    }

    public static float toFloat(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ORDER).getFloat();
    }

    public static byte[] fromInt(final int value) {
        final ByteBuffer buffer = ByteBuffer.allocate(4).order(ORDER);
        buffer.putInt(value);

        return buffer.array();
    }

    public static byte[] fromLong(final long value) {
        final ByteBuffer buffer = ByteBuffer.allocate(8).order(ORDER);
        buffer.putLong(value);

        return buffer.array();
    }

    public static byte[] fromFloat(final float value) {
        final ByteBuffer buffer = ByteBuffer.allocate(4).order(ORDER);
        buffer.putFloat(value);

        return buffer.array();
    }

}