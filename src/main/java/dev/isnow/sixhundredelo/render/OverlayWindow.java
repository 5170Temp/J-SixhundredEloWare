package dev.isnow.sixhundredelo.render;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import dev.isnow.sixhundredelo.sdk.process.internal.User32;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;

public class OverlayWindow {
    private static final String WINDOW_TITLE = "600elo overlay";

    private final RenderSystem renderSystem;
    private long windowHandle;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public OverlayWindow(final RenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_FOCUS_ON_SHOW, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MOUSE_PASSTHROUGH, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 8);
//        GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, 8);
//        GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, 8);
//        GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, 8);
//        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);
//        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 8);

        final long monitor = GLFW.glfwGetPrimaryMonitor();
        final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
        if (vidMode == null) {
            throw new RuntimeException("Failed to get video mode");
        }

        final int width = vidMode.width();
        final int height = vidMode.height();

        windowHandle = GLFW.glfwCreateWindow(width, height, WINDOW_TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        final int supported = GLFW.glfwGetWindowAttrib(windowHandle, GLFW.GLFW_TRANSPARENT_FRAMEBUFFER);
        System.out.println("[OverlayWindow] Transparent framebuffer supported: " + (supported == GLFW.GLFW_TRUE));

        GLFW.glfwSetWindowPos(windowHandle, 0, 0);
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowHandle);

        GL.createCapabilities();

        GL32.glEnable(GL32.GL_BLEND);
        GL32.glBlendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);

        ImGui.createContext();
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NoMouse);

        imGuiGlfw.init(windowHandle, false);
        imGuiGl3.init("#version 150");
        imGuiGl3.createFontsTexture();

        final long hwndLong = GLFWNativeWin32.glfwGetWin32Window(windowHandle);
        final WinDef.HWND hWnd = new WinDef.HWND(new Pointer(hwndLong));

        try {
            Thread.sleep(100);
        } catch (final InterruptedException ignored) {
        }
        User32.makeClickThrough(hWnd);

        System.out.println("[OverlayWindow] Initialized " + width + "x" + height);
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(windowHandle)) {
            GLFW.glfwPollEvents();

            GL32.glClearColor(0f, 0f, 0f, 0f);
            GL32.glClear(GL32.GL_COLOR_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            final float width = ImGui.getIO().getDisplaySizeX();
            final float height = ImGui.getIO().getDisplaySizeY();

            ImGui.setNextWindowPos(0, 0);
            ImGui.setNextWindowSize(width, height);

            final int flags = ImGuiWindowFlags.NoTitleBar
                    | ImGuiWindowFlags.NoResize
                    | ImGuiWindowFlags.NoMove
                    | ImGuiWindowFlags.NoScrollbar
                    | ImGuiWindowFlags.NoInputs
                    | ImGuiWindowFlags.NoBackground
                    | ImGuiWindowFlags.NoBringToFrontOnFocus;

            ImGui.begin("##overlay", flags);
            renderSystem.render(width, height);
            ImGui.end();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            GLFW.glfwSwapBuffers(windowHandle);
        }
    }

    private void cleanup() {
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    public static void launch(final RenderSystem renderSystem) {
        final Thread overlayThread = new Thread(() -> new OverlayWindow(renderSystem).run(), "Overlay-Thread");
        overlayThread.setDaemon(true);
        overlayThread.start();
        System.out.println("[OverlayWindow] Launched");
    }
}
