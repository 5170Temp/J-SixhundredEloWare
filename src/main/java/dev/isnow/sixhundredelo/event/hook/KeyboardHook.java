package dev.isnow.sixhundredelo.event.hook;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import dev.isnow.sixhundredelo.event.EventBus;
import dev.isnow.sixhundredelo.event.events.KeyPressEvent;
import dev.isnow.sixhundredelo.event.events.KeyReleaseEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyboardHook implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(final NativeKeyEvent e) {
        EventBus.getInstance().post(new KeyPressEvent(e.getKeyCode()));
    }

    @Override
    public void nativeKeyReleased(final NativeKeyEvent e) {
        EventBus.getInstance().post(new KeyReleaseEvent(e.getKeyCode()));
    }

    public static void register() {
        final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (final NativeHookException ex) {
            throw new RuntimeException("Failed to register JNativeHook", ex);
        }

        GlobalScreen.addNativeKeyListener(new KeyboardHook());
        System.out.println("[KeyboardHook] Global keyboard hook registered");
    }

    public static void unregister() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (final NativeHookException ex) {
            System.err.println("[KeyboardHook] Failed to unregister: " + ex.getMessage());
        }
    }
}
