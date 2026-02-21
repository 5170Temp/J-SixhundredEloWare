package dev.isnow.sixhundredelo.sdk.process;

import com.sun.jna.Pointer;
import dev.isnow.sixhundredelo.sdk.process.address.MemoryAddress;
import dev.isnow.sixhundredelo.sdk.process.internal.Kernel32;
import dev.isnow.sixhundredelo.sdk.process.internal.MODULEENTRY32;
import lombok.Getter;

@Getter
public class ProcessMemory {
    public static final int VM_READ = 0x0010;
    public static final int VM_WRITE = 0x0020;
    public static final int VM_OPERATION = 0x0008;
    public static final int QUERY_INFORMATION = 0x0400;
    public static final int TH32CS_SNAPMODULE = 0x00000008;
    public static final int TH32CS_SNAPMODULE32 = 0x00000010;

    private final long pid;
    private final Pointer handle;

    public ProcessMemory(final long pid) {
        this.pid = pid;

        this.handle = Kernel32.INSTANCE.OpenProcess(
                VM_READ | VM_WRITE | VM_OPERATION | QUERY_INFORMATION,
                false,
                pid
        );

        if (handle == null) {
            throw new RuntimeException("Cannot open process: " + pid);
        } else {
            System.out.printf("Process " + pid + " injected");
        }
    }

    public MemoryAddress address(final long address) {
        return new MemoryAddress(this, address);
    }

    public byte[] read(final long address, final int size) {
        final byte[] buffer = new byte[size];

        Kernel32.INSTANCE.ReadProcessMemory(
                handle,
                new Pointer(address),
                buffer,
                size,
                null
        );

        return buffer;
    }

    public void write(final long address, final byte[] data) {
        Kernel32.INSTANCE.WriteProcessMemory(
                handle,
                new Pointer(address),
                data,
                data.length,
                null
        );
    }

    public MemoryAddress getModuleBase(final String moduleName) {
        final Pointer snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(TH32CS_SNAPMODULE | TH32CS_SNAPMODULE32, pid);

        if (snapshot == Pointer.NULL) {
            throw new RuntimeException("Snapshot failed");
        }

        final MODULEENTRY32 module = new MODULEENTRY32();
        module.dwSize = module.size();

        try {
            if (Kernel32.INSTANCE.Module32First(snapshot, module)) {
                do {
                    final String name = module.moduleName();
                    if (name.equalsIgnoreCase(moduleName) || name.equalsIgnoreCase(moduleName + ".exe") || name.equalsIgnoreCase(moduleName + ".dll")) {
                        return new MemoryAddress(this, Pointer.nativeValue(module.modBaseAddr));
                    }
                } while (Kernel32.INSTANCE.Module32Next(snapshot, module));
            }
        } finally {
            Kernel32.INSTANCE.CloseHandle(snapshot);
        }

        throw new RuntimeException("Module not found: " + moduleName);
    }

    public void close() {
        Kernel32.INSTANCE.CloseHandle(handle);
    }
}