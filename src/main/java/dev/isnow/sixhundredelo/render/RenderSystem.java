package dev.isnow.sixhundredelo.render;

import imgui.ImDrawList;
import imgui.ImGui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderSystem {
    private final List<Renderable> renderables = new CopyOnWriteArrayList<>();

    public void register(final Renderable renderable) {
        renderables.add(renderable);
    }

    public void render(final float screenWidth, final float screenHeight) {
        final ImDrawList drawList = ImGui.getBackgroundDrawList();

        for (final Renderable renderable : renderables) {
            try {
                renderable.render(drawList, screenWidth, screenHeight);
            } catch (final Exception e) {
                System.err.println("[RenderSystem] Error: " + e.getMessage());
            }
        }
    }
}
