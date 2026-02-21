package dev.isnow.sixhundredelo.sdk.process.internal;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    int MOUSEEVENTF_LEFTDOWN = 0x0002;
    int MOUSEEVENTF_LEFTUP = 0x0004;

    int GWL_EXSTYLE = -20;
    int WS_EX_LAYERED = 0x80000;
    int WS_EX_TRANSPARENT = 0x20;
    int WS_EX_TOPMOST = 0x8;
    int LWA_ALPHA = 0x02;

    void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);

    WinDef.HWND FindWindowA(String lpClassName, String lpWindowName);

    int GetWindowLongA(WinDef.HWND hWnd, int nIndex);

    int SetWindowLongA(WinDef.HWND hWnd, int nIndex, int dwNewLong);

    boolean SetLayeredWindowAttributes(WinDef.HWND hWnd, int crKey, byte bAlpha, int dwFlags);

    static void click() {
        INSTANCE.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        INSTANCE.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
    }

//    static void makeClickThrough(final WinDef.HWND hWnd) {
//        if (hWnd == null)
//            return;
//
//        final int exStyle = INSTANCE.GetWindowLongA(hWnd, GWL_EXSTYLE);
//        INSTANCE.SetWindowLongA(hWnd, GWL_EXSTYLE,
//                exStyle | WS_EX_LAYERED | WS_EX_TRANSPARENT | WS_EX_TOPMOST);
//
//        INSTANCE.SetLayeredWindowAttributes(hWnd, 0, (byte) 0xFF, LWA_ALPHA);
//
//        final Dwmapi.MARGINS margins = new Dwmapi.MARGINS(-1, -1, -1, -1);
//        Dwmapi.INSTANCE.DwmExtendFrameIntoClientArea(hWnd, margins);
//    }

    // ai and still doesnt work
    static void makeClickThrough(final WinDef.HWND hWnd) {
        if (hWnd == null)
            return;

        // Get current extended style
        final int exStyle = INSTANCE.GetWindowLongA(hWnd, GWL_EXSTYLE);

        // Add layered, transparent, topmost
        INSTANCE.SetWindowLongA(hWnd, GWL_EXSTYLE,
                exStyle | WS_EX_LAYERED | WS_EX_TRANSPARENT | WS_EX_TOPMOST);

        // Remove uniform alpha override — let OpenGL per-pixel alpha handle transparency
        // INSTANCE.SetLayeredWindowAttributes(hWnd, 0, (byte) 0xFF, LWA_ALPHA);

        // Remove DWM frame extension — can cause black background
        // final Dwmapi.MARGINS margins = new Dwmapi.MARGINS(-1, -1, -1, -1);
        // Dwmapi.INSTANCE.DwmExtendFrameIntoClientArea(hWnd, margins);
    }
}
