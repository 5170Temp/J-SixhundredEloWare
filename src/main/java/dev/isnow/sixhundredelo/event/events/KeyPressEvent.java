package dev.isnow.sixhundredelo.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KeyPressEvent {
    private final int keyCode;
}
