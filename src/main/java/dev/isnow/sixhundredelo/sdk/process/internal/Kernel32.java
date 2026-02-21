package dev.isnow.sixhundredelo.sdk.process.internal;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary {
    Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

    Pointer OpenProcess(int access, boolean inherit, long pid);

    boolean ReadProcessMemory(
            Pointer hProcess,
            Pointer address,
            byte[] buffer,
            int size,
            IntByReference bytesRead
    );

    boolean WriteProcessMemory(
            Pointer hProcess,
            Pointer address,
            byte[] buffer,
            int size,
            IntByReference bytesWritten
    );

    boolean CloseHandle(Pointer handle);

    Pointer CreateToolhelp32Snapshot(int flags, long pid);

    boolean Module32First(Pointer snapshot, MODULEENTRY32 module);

    boolean Module32Next(Pointer snapshot, MODULEENTRY32 module);

}
