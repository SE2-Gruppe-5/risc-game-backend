package com.se2gruppe5.risikobackend.game.vibrateinterritoryloss;

import static org.junit.jupiter.api.Assertions.*;
import com.se2gruppe5.risikobackend.game.vibrateonterritoryloss.VibrateClientEvent;
import com.se2gruppe5.risikobackend.sse.MessageType;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class VibrateClientEventTest {
    @Test
    void constructorAndGetters_shouldReturnCorrectValues() {
        UUID playerId = UUID.randomUUID();
        int durationMs = 200;
        int intensity = 3;

        VibrateClientEvent event = new VibrateClientEvent(playerId, durationMs, intensity);

        assertEquals(playerId, event.getPlayerId());
        assertEquals(durationMs, event.getDurationMs());
        assertEquals(intensity, event.getIntensity());
    }

    @Test
    void getType_shouldReturnVibrateClientType() {
        VibrateClientEvent event = new VibrateClientEvent(UUID.randomUUID(), 100, 1);
        assertEquals(MessageType.VIBRATE_CLIENT, event.getType());
    }
}
