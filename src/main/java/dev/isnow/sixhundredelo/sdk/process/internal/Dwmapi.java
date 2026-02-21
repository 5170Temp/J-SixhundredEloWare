package dev.isnow.sixhundredelo.sdk.process.internal;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public interface Dwmapi extends StdCallLibrary {
    Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

    int DwmExtendFrameIntoClientArea(WinDef.HWND hWnd, MARGINS pMarInset);

    @Structure.FieldOrder({"cxLeftWidth", "cxRightWidth", "cyTopHeight", "cyBottomHeight"})
    class MARGINS extends Structure {
        public int cxLeftWidth;
        public int cxRightWidth;
        public int cyTopHeight;
        public int cyBottomHeight;

        public MARGINS() {}

        public MARGINS(final int left, final int right, final int top, final int bottom) {
            this.cxLeftWidth = left;
            this.cxRightWidth = right;
            this.cyTopHeight = top;
            this.cyBottomHeight = bottom;
        }
    }
}
