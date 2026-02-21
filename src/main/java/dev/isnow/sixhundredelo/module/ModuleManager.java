package dev.isnow.sixhundredelo.module;

import dev.isnow.sixhundredelo.event.EventBus;
import dev.isnow.sixhundredelo.event.events.FrameEvent;
import lombok.Getter;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Getter
public class ModuleManager {
    private final List<CheatModule> modules = new ArrayList<>();
    private volatile boolean running = false;
    private Thread loopThread;

    public void register(final CheatModule module) {
        modules.add(module);
        System.out.println("[ModuleManager] Registered: " + module.getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends CheatModule> Optional<T> getModule(final Class<T> clazz) {
        return modules.stream()
                .filter(clazz::isInstance)
                .map(m -> (T) m)
                .findFirst();
    }

    public List<CheatModule> getModulesByType(final CheatType type) {
        return modules.stream()
                .filter(m -> m.getCheatType() == type)
                .toList();
    }

    public void start() {
        if (running)
            return;

        running = true;
        loopThread = new Thread(this::loop, "ModuleManager-Loop");
        loopThread.setDaemon(true);
        loopThread.start();

        System.out.println("[ModuleManager] Started with " + modules.size() + " module(s)");
    }

    public void stop() {
        running = false;
        if (loopThread != null) {
            loopThread.interrupt();
        }
    }

    private void loop() {
        long lastTime = System.nanoTime();

        while (running) {
            final long now = System.nanoTime();
            final long delta = now - lastTime;
            lastTime = now;

            EventBus.getInstance().post(new FrameEvent(delta));

            Thread.onSpinWait();
        }
    }
}
