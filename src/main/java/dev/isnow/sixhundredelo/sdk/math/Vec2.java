package dev.isnow.sixhundredelo.sdk.math;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vec2 {
    public static final Vec2 INVALID = new Vec2(-99f, -99f);

    private final float x;
    private final float y;

    public Vec2 add(final Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 subtract(final Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public boolean isValid() {
        return x > -50f && y > -50f;
    }

    @Override
    public String toString() {
        return String.format("Vec2(%.2f, %.2f)", x, y);
    }
}
