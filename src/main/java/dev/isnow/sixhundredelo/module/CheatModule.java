package dev.isnow.sixhundredelo.module;

import dev.isnow.sixhundredelo.event.EventBus;
import dev.isnow.sixhundredelo.event.events.FrameEvent;
import lombok.Getter;
import lombok.Setter;
import net.engio.mbassy.listener.Handler;

@Getter
public abstract class CheatModule {
    private final String name;
    private final CheatType cheatType;

    @Setter
    private boolean enabled = false;

    public CheatModule(final String name, final CheatType cheatType) {
        this.name = name;
        this.cheatType = cheatType;

        EventBus.getInstance().subscribe(this);
    }

    @Handler
    public void onFrame(final FrameEvent event) {
        if (enabled) {
            run();
        }
    }

    public void toggle() {
        enabled = !enabled;
        System.out.println("[" + name + "] " + (enabled ? "Enabled" : "Disabled"));
    }

    public abstract void run();
}
