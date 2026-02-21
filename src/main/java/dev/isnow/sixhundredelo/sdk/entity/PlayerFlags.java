package dev.isnow.sixhundredelo.sdk.entity;

public enum PlayerFlags {
    NONE(0),
    ON_GROUND(1 << 0),
    STANDING(65665),
    CROUCHING(65667);

    private final int value;

    PlayerFlags(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isSet(final int flags) {
        return (flags & value) == value;
    }
}
