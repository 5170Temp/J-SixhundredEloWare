package dev.isnow.sixhundredelo.sdk.entity;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.math.Vec3;
import dev.isnow.sixhundredelo.sdk.offset.Offsets;

public class LocalPlayer extends Entity {

    private final long clientBase;

    public LocalPlayer(final ProcessMemory process, final long pawnAddress, final long clientBase) {
        super(process, pawnAddress);
        this.clientBase = clientBase;
    }

    public int getCrosshairEntityIndex() {
        return process.address(address).offset(Offsets.m_iIDEntIndex).readInt();
    }

    public Vec3 getViewAngles() {
        return process.address(clientBase).offset(Offsets.dwViewAngles).readVec3();
    }

    public boolean isDefusing() {
        return process.address(address).offset(Offsets.m_bIsDefusing).readBool();
    }

    public boolean isInBombZone() {
        return process.address(address).offset(Offsets.m_bInBombZone).readBool();
    }

    public boolean isBuyMenuOpen() {
        return process.address(address).offset(Offsets.m_bIsBuyMenuOpen).readBool();
    }

    public boolean isAttacking() {
        return process.address(clientBase).offset(Offsets.attack).readBool();
    }

    public float getSensitivity() {
        final long sensitivityPtr = process.address(clientBase).offset(Offsets.dwSensitivity).readLong();
        if (sensitivityPtr == 0)
            return 0;
        return process.address(sensitivityPtr).offset(Offsets.dwSensitivity_sensitivity).readFloat();
    }

    public int getAmmo() {
        return process.address(address).offset(Offsets.m_iAmmo).readInt();
    }

    public int getFov() {
        final long cameraServices = process.address(address).offset(Offsets.m_pCameraServices).readLong();
        if (cameraServices == 0)
            return 0;
        return process.address(cameraServices).offset(Offsets.m_iFOV).readInt();
    }

    public long getClippingWeapon() {
        return process.address(address).offset(Offsets.m_pClippingWeapon).readLong();
    }
}
