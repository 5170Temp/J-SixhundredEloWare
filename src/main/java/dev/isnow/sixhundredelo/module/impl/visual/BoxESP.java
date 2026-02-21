package dev.isnow.sixhundredelo.module.impl.visual;

import dev.isnow.sixhundredelo.module.CheatModule;
import dev.isnow.sixhundredelo.module.CheatType;
import dev.isnow.sixhundredelo.render.Renderable;
import dev.isnow.sixhundredelo.sdk.GameContext;
import dev.isnow.sixhundredelo.sdk.entity.Entity;
import dev.isnow.sixhundredelo.sdk.entity.LocalPlayer;
import dev.isnow.sixhundredelo.sdk.math.Vec2;
import dev.isnow.sixhundredelo.sdk.math.Vec3;
import dev.isnow.sixhundredelo.sdk.math.WorldToScreen;
import dev.isnow.sixhundredelo.sdk.offset.Offsets;
import imgui.ImColor;
import imgui.ImDrawList;

import java.util.List;

// taken from the c# cheat
public class BoxESP extends CheatModule implements Renderable {
    public static boolean teamCheck = false;
    public static float boxFillOpacity = 0.2f;
    public static float rounding = 3f;
    public static boolean flashCheck = true;
    public static boolean outerOutline = true;

    public static int enemyColor = ImColor.intToColor(255, 50, 50, 255);
    public static int teamColor = ImColor.intToColor(50, 255, 50, 255);
    public static int enemyFill = ImColor.intToColor(255, 50, 50, 51);
    public static int teamFill = ImColor.intToColor(50, 255, 50, 51);

    private final GameContext context;

    public BoxESP(final GameContext context) {
        super("BoxESP", CheatType.VISUAL);
        this.context = context;
    }

    @Override
    public void run() {}

    @Override
    public void render(final ImDrawList drawList, final float screenWidth, final float screenHeight) {
        if (!isEnabled())
            return;

        final LocalPlayer local = context.getLocalPlayer();
        if (local == null || !local.isAlive())
            return;
        if (flashCheck && local.isFlashed())
            return;

        final float[] viewMatrix = context.getProcess()
                .address(context.getAddresses().getClient())
                .offset(Offsets.dwViewMatrix)
                .readMatrix(16);

        final int localTeam = local.getTeam();
        final List<Entity> players = context.getEntityList().getAllPlayers();

        for (final Entity entity : players) {
            if (entity.getAddress() == local.getAddress())
                continue;
            if (!entity.isAlive())
                continue;

            final int entityTeam = entity.getTeam();
            if (teamCheck && entityTeam == localTeam)
                continue;

            drawBox(drawList, entity, entityTeam == localTeam, viewMatrix, screenWidth, screenHeight);
        }
    }

    private void drawBox(final ImDrawList drawList, final Entity entity, final boolean isTeammate,
            final float[] viewMatrix, final float screenWidth, final float screenHeight) {

        final Vec3 origin = entity.getOrigin();
        final Vec3 head = entity.getEyePosition();

        final Vec2 feetScreen = WorldToScreen.project(viewMatrix, origin, screenWidth, screenHeight);
        final Vec2 headScreen = WorldToScreen.project(viewMatrix, head, screenWidth, screenHeight);

        if (feetScreen == null || headScreen == null)
            return;

        final float entityHeight = feetScreen.getY() - headScreen.getY();
        final float halfWidth = entityHeight / 3f;
        final float centerX = (feetScreen.getX() + headScreen.getX()) / 2f;

        final float topY = headScreen.getY();
        final float bottomY = feetScreen.getY();
        final float left = centerX - halfWidth;
        final float right = centerX + halfWidth;

        final int fill = isTeammate ? teamFill : enemyFill;
        drawList.addRectFilled(left, topY, right, bottomY, fill, rounding);

        if (outerOutline) {
            final int outline = isTeammate ? teamColor : enemyColor;
            drawList.addRect(left, topY, right, bottomY, outline, rounding);
        }
    }
}
