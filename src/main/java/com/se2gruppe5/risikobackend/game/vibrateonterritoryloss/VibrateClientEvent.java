package com.se2gruppe5.risikobackend.game.vibrateonterritoryloss;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.UUID;

public class VibrateClientEvent implements Message {
    private final UUID playerId;
    private final int durationMs;
    private final int intensity;

    public VibrateClientEvent(UUID playerId, int durationMs, int intensity) {
        this.playerId = playerId;
        this.durationMs = durationMs;
        this.intensity = intensity;
    }
    @Override
    public MessageType getType() {
        return MessageType.VIBRATE_CLIENT;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public int getIntensity() {
        return intensity;
    }
}
