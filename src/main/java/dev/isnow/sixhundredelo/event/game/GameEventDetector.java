package dev.isnow.sixhundredelo.event.game;

import dev.isnow.sixhundredelo.event.EventBus;
import dev.isnow.sixhundredelo.event.events.FrameEvent;
import dev.isnow.sixhundredelo.event.events.game.*;
import dev.isnow.sixhundredelo.sdk.GameContext;
import dev.isnow.sixhundredelo.sdk.entity.Entity;
import dev.isnow.sixhundredelo.sdk.entity.LocalPlayer;
import dev.isnow.sixhundredelo.sdk.offset.Offsets;
import net.engio.mbassy.listener.Handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventDetector {
    private final GameContext context;

    private boolean wasBombPlanted = false;
    private float lastBombTimer = 0;
    private int lastShotsFired = 0;
    private boolean wasScoped = false;
    private boolean wasFlashed = false;
    private int lastAliveCount = 0;
    private final Map<Long, Integer> previousHealth = new HashMap<>();

    public GameEventDetector(final GameContext context) {
        this.context = context;
        EventBus.getInstance().subscribe(this);
        System.out.println("[GameEventDetector] Registered");
    }

    @Handler
    public void onFrame(final FrameEvent event) {
        try {
            detectLocalPlayerEvents();
            detectBombEvents();
            detectEntityEvents();
        } catch (final Exception ignored) {
        }
    }

    private void detectLocalPlayerEvents() {
        final LocalPlayer local = context.getLocalPlayer();
        if (local == null || !local.isAlive())
            return;

        final int shots = local.getShotsFired();
        if (shots > lastShotsFired && lastShotsFired >= 0) {
            EventBus.getInstance().post(new LocalPlayerShotFiredEvent(shots));
        }
        lastShotsFired = shots;

        final boolean scoped = local.isScoped();
        if (scoped != wasScoped) {
            EventBus.getInstance().post(new LocalPlayerScopeEvent(scoped));
        }
        wasScoped = scoped;

        final boolean flashed = local.isFlashed();
        if (flashed && !wasFlashed) {
            final float duration = context.getProcess()
                    .address(local.getAddress())
                    .offset(Offsets.m_flFlashBangTime)
                    .readFloat();
            EventBus.getInstance().post(new LocalPlayerFlashEvent(duration));
        }
        wasFlashed = flashed;
    }

    private void detectBombEvents() {
        final long gameRules = context.getProcess()
                .address(context.getAddresses().getClient())
                .offset(Offsets.dwGameRules)
                .readLong();
        if (gameRules == 0)
            return;

        final boolean bombPlanted = context.getProcess()
                .address(gameRules)
                .offset(Offsets.m_bBombPlanted)
                .readBool();

        if (bombPlanted && !wasBombPlanted) {
            final long plantedC4 = context.getProcess()
                    .address(context.getAddresses().getClient())
                    .offset(Offsets.dwPlantedC4)
                    .readLong();
            float blowTime = 0;
            if (plantedC4 != 0) {
                blowTime = context.getProcess()
                        .address(plantedC4)
                        .offset(Offsets.m_flC4Blow)
                        .readFloat();
            }
            EventBus.getInstance().post(new BombPlantedEvent(blowTime));
            lastBombTimer = blowTime;
        }

        if (wasBombPlanted && !bombPlanted && lastBombTimer > 0) {
            EventBus.getInstance().post(new BombExplodedEvent());
            lastBombTimer = 0;
        }

        wasBombPlanted = bombPlanted;
    }

    private void detectEntityEvents() {
        final List<Entity> players = context.getEntityList().getAllPlayers();
        int aliveCount = 0;

        for (final Entity entity : players) {
            final long addr = entity.getAddress();
            final int health = entity.getHealth();
            final Integer prevHealth = previousHealth.get(addr);

            if (prevHealth != null) {
                if (prevHealth > 0 && health <= 0) {
                    EventBus.getInstance().post(new EntityDeathEvent(entity, prevHealth));
                } else if (health < prevHealth && health > 0) {
                    EventBus.getInstance().post(new EntityDamageEvent(entity, prevHealth, health));
                }
            }

            previousHealth.put(addr, health);
            if (health > 0)
                aliveCount++;
        }

        // retarded nigger TODO: make better
        if (aliveCount > lastAliveCount + 3 && lastAliveCount > 0) {
            EventBus.getInstance().post(new RoundStartEvent());
            previousHealth.clear();
        }
        lastAliveCount = aliveCount;
    }
}
