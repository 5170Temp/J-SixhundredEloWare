package dev.isnow.sixhundredelo.sdk.math;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vec3 {
    public static final Vec3 ZERO = new Vec3(0, 0, 0);

    private final float x;
    private final float y;
    private final float z;

    public Vec3 add(final Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 subtract(final Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 scale(final float scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float distanceTo(final Vec3 other) {
        return subtract(other).length();
    }

    public float dot(final Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    @Override
    public String toString() {
        return String.format("Vec3(%.2f, %.2f, %.2f)", x, y, z);
    }
}
