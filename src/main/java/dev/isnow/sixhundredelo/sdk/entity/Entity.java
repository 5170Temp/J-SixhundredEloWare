package dev.isnow.sixhundredelo.sdk.entity;

import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.sdk.math.Vec3;
import dev.isnow.sixhundredelo.sdk.offset.Offsets;
import lombok.Getter;

@Getter
public class Entity {
    private static final int LIFE_STATE_ALIVE = 256;

    protected final ProcessMemory process;
    protected final long address;

    public Entity(final ProcessMemory process, final long address) {
        this.process = process;
        this.address = address;
    }

    public boolean isValid() {
        return address != 0;
    }

    public int getHealth() {
        return process.address(address).offset(Offsets.m_iHealth).readInt();
    }

    public int getTeam() {
        return process.address(address).offset(Offsets.m_iTeamNum).readInt();
    }

    public int getLifeState() {
        return process.address(address).offset(Offsets.m_lifeState).readInt();
    }

    public boolean isAlive() {
        return getHealth() > 0 && getLifeState() == LIFE_STATE_ALIVE;
    }

    public int getArmor() {
        return process.address(address).offset(Offsets.m_ArmorValue).readInt();
    }

    public int getFlags() {
        return process.address(address).offset(Offsets.m_fFlags).readInt();
    }

    public boolean isSameTeam(final Entity other) {
        return getTeam() == other.getTeam();
    }

    public Vec3 getOrigin() {
        return process.address(address).offset(Offsets.m_vOldOrigin).readVec3();
    }

    public Vec3 getViewOffset() {
        return process.address(address).offset(Offsets.m_vecViewOffset).readVec3();
    }

    public Vec3 getEyePosition() {
        return getOrigin().add(getViewOffset());
    }

    public Vec3 getVelocity() {
        return process.address(address).offset(Offsets.m_vecAbsVelocity).readVec3();
    }

    public Vec3 getAbsOrigin() {
        return process.address(address).offset(Offsets.m_vecAbsOrigin).readVec3();
    }

    public Vec3 getAimPunchAngle() {
        return process.address(address).offset(Offsets.m_aimPunchAngle).readVec3();
    }

    public Vec3 getEyeAngles() {
        return process.address(address).offset(Offsets.m_angEyeAngles).readVec3();
    }

    public int getShotsFired() {
        return process.address(address).offset(Offsets.m_iShotsFired).readInt();
    }

    public boolean isScoped() {
        return process.address(address).offset(Offsets.m_bIsScoped).readBool();
    }

    public boolean isFlashed() {
        return process.address(address).offset(Offsets.m_flFlashBangTime).readFloat() > 1.5f;
    }

    public boolean isWalking() {
        return process.address(address).offset(Offsets.m_bIsWalking).readBool();
    }

    public boolean isSpotted() {
        return process.address(address)
                .offset(Offsets.m_entitySpottedState)
                .offset(Offsets.m_bSpotted)
                .readBool();
    }

    public long getGameSceneNode() {
        return process.address(address).offset(Offsets.m_pGameSceneNode).readLong();
    }

    public long getBoneMatrix() {
        final long sceneNode = getGameSceneNode();
        if (sceneNode == 0)
            return 0;
        return process.address(sceneNode).offset(Offsets.m_modelState + 0x80).readLong();
    }

    public float distanceTo(final Entity other) {
        return getOrigin().distanceTo(other.getOrigin());
    }

    public boolean isOnGround() {
        return PlayerFlags.ON_GROUND.isSet(getFlags());
    }

    public void setFlashBangTime(final float value) {
        writeFloat(Offsets.m_flFlashBangTime, value);
    }

    public void setSpotted(final boolean value) {
        process.address(address)
                .offset(Offsets.m_entitySpottedState)
                .offset(Offsets.m_bSpotted)
                .writeInt(value ? 1 : 0);
    }

    public void setFlags(final int value) {
        writeInt(Offsets.m_fFlags, value);
    }

    private void writeInt(final long offset, final int value) {
        process.address(address).offset(offset).writeInt(value);
    }

    private void writeFloat(final long offset, final float value) {
        process.address(address).offset(offset).writeFloat(value);
    }
}
