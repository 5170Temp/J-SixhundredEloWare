package dev.isnow.sixhundredelo.module.impl.rage;

import dev.isnow.sixhundredelo.event.events.KeyPressEvent;
import dev.isnow.sixhundredelo.event.events.KeyReleaseEvent;
import dev.isnow.sixhundredelo.module.CheatModule;
import dev.isnow.sixhundredelo.module.CheatType;
import dev.isnow.sixhundredelo.sdk.process.internal.User32;
import dev.isnow.sixhundredelo.sdk.GameContext;
import dev.isnow.sixhundredelo.sdk.entity.Entity;
import dev.isnow.sixhundredelo.sdk.entity.LocalPlayer;
import dev.isnow.sixhundredelo.sdk.util.NanoTimer;
import net.engio.mbassy.listener.Handler;

import java.util.concurrent.ThreadLocalRandom;

public class TriggerBot extends CheatModule {
    public static int minDelay = 0;
    public static int maxDelay = 10;
    public static boolean teamCheck = true;
    public static int triggerKey = 56;
    public static boolean requireKeybind = true;
    public static int gracePeriodMs = 100;

    private volatile boolean triggerKeyHeld = false;

    private final Object stateLock = new Object();
    private boolean onTarget = false;
    private int currentDelay = 0;

    private final NanoTimer reacquireTimer = new NanoTimer();
    private final NanoTimer graceTimer = new NanoTimer();

    private final GameContext context;

    public TriggerBot(final GameContext context) {
        super("TriggerBot", CheatType.RAGE);
        this.context = context;
    }

    @Handler
    public void onKeyPress(final KeyPressEvent event) {
        if (event.getKeyCode() == triggerKey) {
            triggerKeyHeld = true;
        }
    }

    @Handler
    public void onKeyRelease(final KeyReleaseEvent event) {
        if (event.getKeyCode() == triggerKey) {
            triggerKeyHeld = false;
            clearTargetState();
        }
    }

    @Override
    public void run() {
        try {
            if (requireKeybind && !triggerKeyHeld) {
                clearTargetState();
                return;
            }

            final LocalPlayer local = context.getLocalPlayer();
            if (local == null || !local.isAlive()) {
                clearTargetState();
                return;
            }

            final int crosshairIdx = local.getCrosshairEntityIndex();
            if (crosshairIdx <= 0) {
                clearTargetState();
                return;
            }

            final Entity target = context.getEntityList().getByIndex(crosshairIdx);
            if (target == null || !target.isAlive()) {
                clearTargetState();
                return;
            }

            if (teamCheck && local.isSameTeam(target)) {
                clearTargetState();
                return;
            }

            handleShooting();
        } catch (final Exception ex) {
            System.err.println("[TriggerBot] Error: " + ex.getMessage());
            clearTargetState();
        }
    }

    private void handleShooting() {
        synchronized (stateLock) {
            if (!onTarget) {
                if (!reacquireTimer.isRunning()) {
                    reacquireTimer.restart();
                    currentDelay = ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1);
                }
                if (reacquireTimer.elapsedMs() >= currentDelay) {
                    User32.click();
                    onTarget = true;
                    reacquireTimer.reset();
                    graceTimer.restart();
                }
            } else {
                User32.click();
                graceTimer.restart();
            }
        }
    }

    private void clearTargetState() {
        synchronized (stateLock) {
            if (onTarget && graceTimer.isRunning() && graceTimer.elapsedMs() < gracePeriodMs)
                return;

            onTarget = false;
            currentDelay = 0;
            reacquireTimer.reset();
            graceTimer.reset();
        }
    }
}
