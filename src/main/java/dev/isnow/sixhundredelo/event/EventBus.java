package dev.isnow.sixhundredelo.event;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();

    private final MBassador<Object> bus;

    private EventBus() {
        this.bus = new MBassador<>(new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                .addFeature(Feature.AsynchronousMessageDispatch.Default()));
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void subscribe(final Object listener) {
        bus.subscribe(listener);
    }

    public void unsubscribe(final Object listener) {
        bus.unsubscribe(listener);
    }

    public void post(final Object event) {
        bus.post(event).now();
    }
}
