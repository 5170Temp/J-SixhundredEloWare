package dev.isnow.sixhundredelo;

import dev.isnow.sixhundredelo.event.game.GameEventDetector;
import dev.isnow.sixhundredelo.event.hook.KeyboardHook;
import dev.isnow.sixhundredelo.module.ModuleManager;
import dev.isnow.sixhundredelo.module.impl.rage.TriggerBot;
import dev.isnow.sixhundredelo.module.impl.visual.BoxESP;
import dev.isnow.sixhundredelo.module.impl.visual.NoFlash;
import dev.isnow.sixhundredelo.sdk.process.ProcessMemory;
import dev.isnow.sixhundredelo.render.OverlayWindow;
import dev.isnow.sixhundredelo.render.RenderSystem;
import dev.isnow.sixhundredelo.sdk.GameAddresses;
import dev.isnow.sixhundredelo.sdk.GameContext;
import dev.isnow.sixhundredelo.sdk.offset.loader.OffsetLoader;
import lombok.Getter;

@Getter
public class SixhundredElo {
    private final ProcessMemory process;
    private final GameContext gameContext;
    private final ModuleManager moduleManager;
    private final RenderSystem renderSystem;

    public SixhundredElo(final ProcessMemory process) {
        this.process = process;
        this.moduleManager = new ModuleManager();
        this.renderSystem = new RenderSystem();

        OffsetLoader.loadOffsets();

        final GameAddresses addresses = new GameAddresses(process);
        this.gameContext = new GameContext(process, addresses);

        KeyboardHook.register();
        Runtime.getRuntime().addShutdownHook(new Thread(KeyboardHook::unregister));

        new GameEventDetector(gameContext);

        moduleManager.register(new TriggerBot(gameContext));
        moduleManager.register(new NoFlash(gameContext));

        final BoxESP boxESP = new BoxESP(gameContext);
        moduleManager.register(boxESP);
        renderSystem.register(boxESP);

        moduleManager.start();

        moduleManager.getModule(TriggerBot.class).get().setEnabled(true);
        moduleManager.getModule(NoFlash.class).get().setEnabled(true);
        moduleManager.getModule(BoxESP.class).get().setEnabled(true);

        OverlayWindow.launch(renderSystem);
    }
}
