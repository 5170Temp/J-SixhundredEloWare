package dev.isnow.sixhundredelo.event.events.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LocalPlayerShotFiredEvent {
    private final int totalShots;
}
