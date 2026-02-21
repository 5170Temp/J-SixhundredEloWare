package dev.isnow.sixhundredelo.sdk.process.internal;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class MODULEENTRY32 extends Structure {
    public int dwSize;
    public int th32ModuleID;
    public int th32ProcessID;
    public int GlblcntUsage;
    public int ProccntUsage;

    public Pointer modBaseAddr;
    public int modBaseSize;
    public Pointer hModule;

    public byte[] szModule = new byte[256];
    public byte[] szExePath = new byte[260];

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(
                "dwSize",
                "th32ModuleID",
                "th32ProcessID",
                "GlblcntUsage",
                "ProccntUsage",
                "modBaseAddr",
                "modBaseSize",
                "hModule",
                "szModule",
                "szExePath"
        );
    }

    public String moduleName() {
        return Native.toString(szModule);
    }

    public String exePath() {
        return Native.toString(szExePath);
    }
}
