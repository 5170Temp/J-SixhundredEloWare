package dev.isnow.sixhundredelo.event.events.game;

import dev.isnow.sixhundredelo.sdk.entity.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EntityDeathEvent {
    private final Entity entity;
    private final int previousHealth;
}
