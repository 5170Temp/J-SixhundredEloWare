package dev.isnow.sixhundredelo.sdk.math;

public class WorldToScreen {

    public static Vec2 project(final float[] viewMatrix, final Vec3 world, final float screenWidth,
            final float screenHeight) {
        final float w = viewMatrix[3] * world.getX() + viewMatrix[7] * world.getY() + viewMatrix[11] * world.getZ()
                + viewMatrix[15];

        if (w < 0.001f)
            return null;

        float x = viewMatrix[0] * world.getX() + viewMatrix[4] * world.getY() + viewMatrix[8] * world.getZ()
                + viewMatrix[12];
        float y = viewMatrix[1] * world.getX() + viewMatrix[5] * world.getY() + viewMatrix[9] * world.getZ()
                + viewMatrix[13];

        x /= w;
        y /= w;

        final float screenX = (screenWidth / 2f) + (x * screenWidth / 2f);
        final float screenY = (screenHeight / 2f) - (y * screenHeight / 2f);

        return new Vec2(screenX, screenY);
    }
}
