package dev.isnow.sixhundredelo.module.impl.visual;

import dev.isnow.sixhundredelo.event.events.game.LocalPlayerFlashEvent;
import dev.isnow.sixhundredelo.module.CheatModule;
import dev.isnow.sixhundredelo.module.CheatType;
import dev.isnow.sixhundredelo.sdk.GameContext;
import dev.isnow.sixhundredelo.sdk.entity.LocalPlayer;
import net.engio.mbassy.listener.Handler;

public class NoFlash extends CheatModule {
    private final GameContext gameContext;

    public NoFlash(final GameContext gameContext) {
        super("NoFlash", CheatType.VISUAL);
        this.gameContext = gameContext;
    }

    @Override
    public void run() {}

    @Handler
    public void onFlash(final LocalPlayerFlashEvent event) {
        final LocalPlayer local = gameContext.getLocalPlayer();
        if (local != null) {
            local.setFlashBangTime(0f);
        }
    }
}
