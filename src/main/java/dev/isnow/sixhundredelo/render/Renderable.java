package dev.isnow.sixhundredelo.render;

import imgui.ImDrawList;

public interface Renderable {
    void render(ImDrawList drawList, float screenWidth, float screenHeight);
}
